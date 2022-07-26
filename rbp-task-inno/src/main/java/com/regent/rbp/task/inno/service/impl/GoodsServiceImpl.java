package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.omiChannel.OnlineGoods;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.service.onlinePlatform.OnlineGoodsService;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.GoodsItemDto;
import com.regent.rbp.task.inno.model.dto.GoodsSearchDto;
import com.regent.rbp.task.inno.model.dto.GoodsSearchPageDto;
import com.regent.rbp.task.inno.model.dto.SkuDto;
import com.regent.rbp.task.inno.model.param.GoodsDownloadOnlineGoodsParam;
import com.regent.rbp.task.inno.model.req.GoodsSearchReqDto;
import com.regent.rbp.task.inno.model.resp.GoodsSearchRespDto;
import com.regent.rbp.task.inno.service.GoodsService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author xuxing
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final String GET_GOODS_LIST = "api/BasicData/Get_Goods_List_WithDetail";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BarcodeDao barcodeDao;

    @Autowired
    private OnlinePlatformDao onlinePlatformDao;

    @Autowired
    private OnlineGoodsService onlineGoodsService;

    /**
     * 转换参数(电商平台编号)
     * @param goodsDownloadOnlineGoodsParam
     * @return
     */
    @Override
    public Long getOnlinePlatformId(GoodsDownloadOnlineGoodsParam goodsDownloadOnlineGoodsParam) {
        String onlinePlatformCode = goodsDownloadOnlineGoodsParam.getOnlinePlatformCode();

        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().select("id", "code", "name")
                .eq("code", onlinePlatformCode));
        return onlinePlatform.getId();
    }

    /**
     * 拉取APP商品列表
     * @param onlinePlatform
     */
    @Override
    public void downloadGoodsList(OnlinePlatform onlinePlatform) {
        Long onlinePlatformId = onlinePlatform.getId();
        GoodsSearchReqDto requestDto = new GoodsSearchReqDto();
        requestDto.setApp_key(onlinePlatform.getAppKey());
        requestDto.setApp_secrept(onlinePlatform.getAppSecret());

        int pageIndex = 1;
        GoodsSearchDto searchDto = new GoodsSearchDto();
        searchDto.setPageSize(100);
        searchDto.setTxtKey("");
        searchDto.setOrderByType("desc");
        searchDto.setOrderwhere("2");
        //上架状态，0下架，1上架， 2全部
        searchDto.setOnSaleStatus(2);
        requestDto.setData(searchDto);
        while (true) {
            searchDto.setPageIndex(pageIndex);
            XxlJobHelper.log(String.format("查询第%s页线上货品", pageIndex));

            String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), GET_GOODS_LIST);
            String result = HttpUtil.post(api_url, JSON.toJSONString(requestDto));

            GoodsSearchRespDto responseDto = JSON.parseObject(result, GoodsSearchRespDto.class);
            if(responseDto == null || responseDto.getData() == null) {
                XxlJobHelper.log(result);
                break;
            }
            GoodsSearchPageDto goodsSearchPageDto = responseDto.getData();
            if(goodsSearchPageDto == null || goodsSearchPageDto.getData() == null) {
                XxlJobHelper.log(result);
                break;
            }
            List<GoodsItemDto> goodsList = goodsSearchPageDto.getData();
            if(goodsList.size() == 0) {
                XxlJobHelper.log(result);
                break;
            }
            XxlJobHelper.log(String.format("获取线上商品：%s",goodsList.size()));
            List<OnlineGoods> insertOnlineGoodsList = new ArrayList<>(goodsList.size());
            List<OnlineGoods> updateOnlineGoodsList = new ArrayList<>(goodsList.size());
            for (GoodsItemDto goodsItemDto : goodsList) {
                for(SkuDto skuDto : goodsItemDto.getList_sku_property()) {
                    OnlineGoods item = new OnlineGoods();
                    item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    item.setOnlinePlatformId(onlinePlatformId);
                    item.setOnlineGoodsId(Integer.toString(goodsItemDto.getGoods_id()));
                    item.setOnlineGoodsCode(goodsItemDto.getGoods_sn());
                    item.setOnlineGoodsName(goodsItemDto.getGoods_name());
                    item.setOnlineOnsaleFlag(goodsItemDto.getIsOnSale());
                    item.setOnlineOnsaleDate(DateUtil.getDate(goodsItemDto.getOnSaleTime(), DateUtil.FULL_DATE_FORMAT));

                    item.setBarcode(skuDto.getSku());
                    item.setOnlineBarcode(skuDto.getSku());
                    item.setOnlineQuantity(new BigDecimal(skuDto.getProduct_number()));
                    item.setOnlinePrice(new BigDecimal(skuDto.getSku_sale_price()));

                    /** abnormalFlag 异常标记;0.无异常;1.有异常 */
                    int count = barcodeDao.selectCount(new QueryWrapper<Barcode>().eq("barcode", skuDto.getSku()));
                    if(count > 0) {
                        item.setAbnormalFlag(false);
                        item.setAbnormalMessage("");
                    } else {
                        item.setAbnormalFlag(true);
                        item.setAbnormalMessage("条码不存在");
                        XxlJobHelper.log(String.format("%s条码不存在", skuDto.getSku()));
                    }
                    List<OnlineGoods> existsRecords = onlineGoodsService.list(new QueryWrapper<OnlineGoods>()
                            .select("id").eq("online_platform_id", onlinePlatformId)
                            .eq("barcode", item.getBarcode()));
                    if(existsRecords.size() > 0) {
                        OnlineGoods existOnlineGoods = existsRecords.get(0);
                        item.setId(existOnlineGoods.getId());
                        updateOnlineGoodsList.add(item);
                    } else {
                        insertOnlineGoodsList.add(item);
                    }
                }
            }
            onlineGoodsService.saveOrUpdateList(updateOnlineGoodsList, insertOnlineGoodsList);
            XxlJobHelper.log(String.format("更新线上商品：%s,插入线上商品：%s",updateOnlineGoodsList.size(), insertOnlineGoodsList.size()));

            int totalPages = Integer.parseInt(goodsSearchPageDto.getTotalPages());
            if(pageIndex >= totalPages) {
                break;
            }
            pageIndex++;
        }
    }
}
