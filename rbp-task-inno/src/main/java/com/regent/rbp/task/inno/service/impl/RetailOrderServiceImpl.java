package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.RetailOrderGoodsDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderItemDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderMainDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderSearchDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderSearchPageDto;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.model.req.RetailOrderSearchReqDto;
import com.regent.rbp.task.inno.model.resp.RetailOrderSearchRespDto;
import com.regent.rbp.task.inno.service.RetailOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/22
 * @description
 */
@Service
public class RetailOrderServiceImpl implements RetailOrderService {

    private static final String POST_GET_APP_ORDER_LIST = "api/Order/Post_Get_App_Order_Info";

    @Autowired
    private InnoConfig innoConfig;
    @Autowired
    private RetailOrderBillService retailOrderBillService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;

    /**
     * 拉取订单列表
     *
     * @param param
     */
    @Transactional
    @Override
    public void downloadOnlineOrderList(RetailOrderDownloadOnlineOrderParam param, OnlinePlatform onlinePlatform) {
        RetailOrderSearchReqDto searchReqDto = new RetailOrderSearchReqDto();
        searchReqDto.setApp_key(innoConfig.getAppkey());
        searchReqDto.setApp_secrept(innoConfig.getAppsecret());
        int pageIndex = 1;
        // 记录最后一条记录时间
        String lastTimeStr = param.getEndTime();
        try {
            // 获取销售渠道编号
            String channelCode = baseDbDao.getStringDataBySql(String.format("select code from rbp_channel where id = %s", onlinePlatform.getChannelId()));

            RetailOrderSearchDto searchDto = new RetailOrderSearchDto();
            searchReqDto.setData(searchDto);
            searchDto.setBeginTime(param.getBeginTime());
            searchDto.setEndTime(param.getEndTime());
            searchDto.setOrder_sn_list(param.getOrder_sn_list());
            // 循环拉取订单列表
            while (true) {
                searchDto.setPageIndex(pageIndex);

                String api_url = String.format("%s%s", innoConfig.getUrl(), POST_GET_APP_ORDER_LIST);
                String result = HttpUtil.post(api_url, JSON.toJSONString(searchReqDto));
                // 装换
                RetailOrderSearchRespDto responseDto = JSON.parseObject(result, RetailOrderSearchRespDto.class);
                if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
                    XxlJobHelper.log(responseDto.getMsg());
                    break;
                }
                if (responseDto == null || responseDto.getData() == null) {
                    break;
                }
                RetailOrderSearchPageDto pageDto = responseDto.getData();
                if (pageDto == null || pageDto.getData() == null) {
                    break;
                }
                List<RetailOrderMainDto> orderList = pageDto.getData();
                if (orderList.size() == 0) {
                    break;
                }
                // 循环创建全渠道订单
                for (RetailOrderMainDto mainDto : orderList) {
                    RetailOrderBillSaveParam saveParam = new RetailOrderBillSaveParam();
                    // 参数转换
                    this.convertOrder(mainDto, saveParam);
                    saveParam.setOnlinePlatformCode(param.getOnlinePlatformCode());
                    saveParam.setRetailChannelNo(channelCode);
                    // 插入订单
                    ModelDataResponse<String> response = retailOrderBillService.save(saveParam);
                    // 返回结果
                    if (ResponseCode.OK != response.getCode()) {
                        XxlJobHelper.log(String.format("订单号：%s，失败原因：%s", saveParam.getOnlineOrderCode(), response.getMessage()));
                    }
                }
                // 处理查询分页
                int totalPages = Integer.parseInt(pageDto.getTotalPages());
                if (pageIndex >= totalPages) {
                    lastTimeStr = orderList.get(orderList.size() - 1).getOrderinfo().getLastModifiedTime();
                    break;
                }
                pageIndex++;
            }
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        } finally {
            // 记录接口的最大拉取时间
            OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                    .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatform.getId()).eq(OnlinePlatformSyncCache::getSyncKey, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB));
            if (null != syncCache) {
                syncCache.setData(lastTimeStr);
                onlinePlatformSyncCacheDao.updateById(syncCache);
            }
        }

    }

    /**
     * 参数转换器
     */
    private void convertOrder(RetailOrderMainDto sourceDto, RetailOrderBillSaveParam targetDto) {
        RetailOrderItemDto order = sourceDto.getOrderinfo();
        List<RetailOrderGoodsDto> goods = sourceDto.getOrdergoods();
        List<RetailOrderBillGoodsDetailData> goodsDetailData = new ArrayList<>();
        targetDto.setGoodsDetailData(goodsDetailData);
        /********************** 订单主体 ******************************/
        targetDto.setBillDate(DateUtil.getDate(order.getAdd_time(), DateUtil.SHORT_DATE_FORMAT));
        targetDto.setManualNo(order.getOrder_sn());
        targetDto.setAcceptGoodsCode(order.getPickup_code());
        targetDto.setOnlineOrderCode(order.getOrder_sn());
        targetDto.setOnlinePlatformTypeId(1);
        // 状态转换
        targetDto.setStatus(StatusEnum.NONE.getStatus());
        if ("0".equals(order.getPay_status())) {
            targetDto.setOnlineStatus(0);
        } else {
            targetDto.setOnlineStatus(3);
        }
        targetDto.setEmployeeName(order.getStaff_code());
        targetDto.setBuyerNotes(order.getUser_remark());
        targetDto.setSellerNotes(StrUtil.EMPTY);
        targetDto.setNotes(order.getAdmin_remark());
        /********************** 物流信息 ******************************/
        targetDto.setMemberCardCode(order.getCard_num());
        targetDto.setBuyerNickname(order.getUser_name());
        targetDto.setBuyerAccount(order.getUser_name());
        targetDto.setBuyerEmail(StrUtil.EMPTY);
        targetDto.setLogisticsCompanyCode(order.getShipping_code());
        targetDto.setNation(SystemConstants.NATION);
        targetDto.setProvince(order.getProvince());
        targetDto.setCity(order.getCity());
        targetDto.setCounty(order.getDistinct());
        targetDto.setAddress(order.getAddress());
        targetDto.setContactsPerson(order.getConsigee());
        targetDto.setMobile(order.getMobile());
        targetDto.setPostCode(order.getZip());
        targetDto.setLogisticsAmount(new BigDecimal(order.getShipping_fee()));
        targetDto.setNote(StrUtil.EMPTY);
        /********************** 货品明细信息 ******************************/
        if (CollUtil.isNotEmpty(goods)) {
            for (RetailOrderGoodsDto goodsDto : goods) {
                RetailOrderBillGoodsDetailData item = new RetailOrderBillGoodsDetailData();
                goodsDetailData.add(item);
                item.setBarcode(goodsDto.getProduct_sn());
                item.setSaleType(0);
                item.setRetailGoodsType(0);
                item.setBalancePrice(new BigDecimal(goodsDto.getGoods_real_price()));
                item.setDiscount(item.getBalancePrice().divide(new BigDecimal(goodsDto.getMarket_price()), 4, BigDecimal.ROUND_HALF_UP));
                item.setQuantity(new BigDecimal(goodsDto.getGoods_num()));
                item.setRemark(StrUtil.EMPTY);
            }
        }
    }

}
