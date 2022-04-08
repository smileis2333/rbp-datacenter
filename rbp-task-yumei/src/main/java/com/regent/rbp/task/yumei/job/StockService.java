package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.common.dao.StockDetailDao;
import com.regent.rbp.common.dao.UsableStockDetailDao;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.MD5Util;
import com.regent.rbp.task.yumei.job.dao.YumeiStockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/04/08
 * @description
 */
@Service
public class StockService {
    @Autowired
    private UsableStockDetailDao usableStockDetailDao;
    @Autowired
    private YumeiStockDao stockDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private StockDetailDao stockDetailDao;

    @Transactional
    public void settingStock(Set<UsableStockDetail> usableStockDetailList, Set<StockDetail> stockDetailSet) {
        overwriteUsableStockDetail(usableStockDetailList);
        overwriteStockDetail(stockDetailSet);
    }

    @Transactional
    public void overwriteUsableStockDetail(Set<UsableStockDetail> usableStockDetailList) {
        List<UsableStockDetail> insertUsableStockDetailList = new ArrayList<>();
        List<UsableStockDetail> updateUsableStockDetailList = new ArrayList<>();
        if (CollUtil.isEmpty(usableStockDetailList)) {
            return;
        }
        // 原可用库存
        Map<String, UsableStockDetail> stockDetailMap = new HashMap<>();
        List<UsableStockDetail> stockDetailList = usableStockDetailDao.selectList(new QueryWrapper<UsableStockDetail>().in("hash_code", usableStockDetailList.stream().map(v -> getHashCode(v)).collect(Collectors.toSet())));
        if (CollUtil.isNotEmpty(stockDetailList)) {
            stockDetailMap = stockDetailList.stream().collect(Collectors.toMap(v -> v.getHashCode(), Function.identity(), (x1, x2) -> x1));
        }
        //hashInsert记录插入操作的实例，解决同款多价的多个相同sku的记录。
        ConcurrentHashMap<String, UsableStockDetail> hashInsert = new ConcurrentHashMap<>(usableStockDetailList.size());
        for (UsableStockDetail usableStockDetail : usableStockDetailList) {
            String hashCode = getHashCode(usableStockDetail);
            String skuHashCode = getSkuHashCode(usableStockDetail);
            UsableStockDetail dbUsableStockDetail = stockDetailMap.get(hashCode);
            if (null == dbUsableStockDetail && !hashInsert.containsKey(hashCode)) {
                Long id = systemCommonService.getId();
                usableStockDetail.setId(id);
                usableStockDetail.preUpdate();
                usableStockDetail.setHashCode(hashCode);
                usableStockDetail.setSkuHashCode(skuHashCode);
                insertUsableStockDetailList.add(usableStockDetail);
            } else {
                if (hashInsert.containsKey(hashCode) && dbUsableStockDetail == null) {
                    //同款多价的场景，找到之前插入的那条数据。
                    dbUsableStockDetail = hashInsert.get(hashCode);
                }
                usableStockDetail.setId(dbUsableStockDetail.getId());
                updateUsableStockDetailList.add(usableStockDetail);
            }
        }
        //新增
        if (CollUtil.isNotEmpty(insertUsableStockDetailList)) {
            int insertRow = usableStockDetailDao.insertUsableStockDetailList(insertUsableStockDetailList, false);
            if (insertRow != insertUsableStockDetailList.size()) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "10101");
            }
        }
        //修改
        if (CollUtil.isNotEmpty(updateUsableStockDetailList)) {
            int updateRow = stockDao.overwriteUsableStockDetailList(updateUsableStockDetailList);
            if (updateRow != updateUsableStockDetailList.size()) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "10101");
            }
        }

    }

    @Transactional
    public void overwriteStockDetail(Set<StockDetail> stockDetailSet) {
        List<StockDetail> insertStockDetailList = new ArrayList<>();
        List<StockDetail> updateStockDetailList = new ArrayList<>();
        if (CollUtil.isNotEmpty(stockDetailSet)) {
            //hashInsert记录插入操作的实例，解决同款多价的多个相同sku的记录。
            ConcurrentHashMap<String, StockDetail> hashInsert = new ConcurrentHashMap<>(stockDetailSet.size());
            for (StockDetail stockDetail : stockDetailSet) {
                String hashCode = getHashCode(stockDetail);
                String skuHashCode = getSkuHashCode(stockDetail);
                StockDetail dbStockDetail = stockDetailDao.selectOne(new QueryWrapper<StockDetail>().eq("hash_code", hashCode));
                //不存在
                if (null == dbStockDetail && !hashInsert.containsKey(hashCode)) {
                    Long id = systemCommonService.getId();
                    stockDetail.setId(id);
                    stockDetail.preUpdate();
                    stockDetail.setHashCode(hashCode);
                    stockDetail.setSkuHashCode(skuHashCode);
                    insertStockDetailList.add(stockDetail);
                    hashInsert.put(hashCode, stockDetail);
                } else {
                    if (hashInsert.containsKey(hashCode) && dbStockDetail == null) {
                        //同款多价的场景，找到之前插入的那条数据。
                        dbStockDetail = hashInsert.get(hashCode);
                    }
                    stockDetail.setId(dbStockDetail.getId());
                    updateStockDetailList.add(stockDetail);
                }
            }
        }

        //新增
        if (CollUtil.isNotEmpty(insertStockDetailList)) {
            int insertRow = stockDetailDao.insertStockDetailList(insertStockDetailList, false);
            if (insertRow != insertStockDetailList.size()) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "10101");
            }
        }
        //修改
        if (CollUtil.isNotEmpty(updateStockDetailList)) {
            int updateRow = stockDao.overwriteStockDetailList(updateStockDetailList);
            if (updateRow != updateStockDetailList.size()) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "10101");
            }
        }
    }

    private String getHashCode(UsableStockDetail usableStockDetail) {
        String channelIdStr = usableStockDetail.getChannelId().toString();
        String goodsIdStr = usableStockDetail.getGoodsId().toString();
        String colorIdStr = usableStockDetail.getColorId().toString();
        String longIdStr = usableStockDetail.getLongId().toString();
        String sizeIdStr = usableStockDetail.getSizeId().toString();
        return MD5Util.md5(channelIdStr.concat(goodsIdStr).concat(colorIdStr).concat(longIdStr).concat(sizeIdStr));
    }

    private String getSkuHashCode(UsableStockDetail usableStockDetail) {
        String goodsIdStr = usableStockDetail.getGoodsId().toString();
        String colorIdStr = usableStockDetail.getColorId().toString();
        String longIdStr = usableStockDetail.getLongId().toString();
        String sizeIdStr = usableStockDetail.getSizeId().toString();
        return MD5Util.md5(goodsIdStr.concat(colorIdStr).concat(longIdStr).concat(sizeIdStr));
    }

    private String getHashCode(StockDetail stockDetail) {
        String channelIdStr = stockDetail.getChannelId().toString();
        String goodsIdStr = stockDetail.getGoodsId().toString();
        String colorIdStr = stockDetail.getColorId().toString();
        String longIdStr = stockDetail.getLongId().toString();
        String sizeIdStr = stockDetail.getSizeId().toString();
        return MD5Util.md5(channelIdStr.concat(goodsIdStr).concat(colorIdStr).concat(longIdStr).concat(sizeIdStr));
    }

    private String getSkuHashCode(StockDetail stockDetail) {
        String goodsIdStr = stockDetail.getGoodsId().toString();
        String colorIdStr = stockDetail.getColorId().toString();
        String longIdStr = stockDetail.getLongId().toString();
        String sizeIdStr = stockDetail.getSizeId().toString();
        return MD5Util.md5(goodsIdStr.concat(colorIdStr).concat(longIdStr).concat(sizeIdStr));
    }

}
