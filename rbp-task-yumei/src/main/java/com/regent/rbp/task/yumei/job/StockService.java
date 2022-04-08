package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.common.dao.UsableStockDetailDao;
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

}
