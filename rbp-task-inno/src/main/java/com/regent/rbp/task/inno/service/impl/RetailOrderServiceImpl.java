package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillDstbInfo;
import com.regent.rbp.api.dto.retail.RetailOrderBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.dto.*;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.model.param.RetailOrderStatusDownloadParam;
import com.regent.rbp.task.inno.model.req.RetailOrderSearchReqDto;
import com.regent.rbp.task.inno.model.resp.RetailOrderSearchRespDto;
import com.regent.rbp.task.inno.model.resp.RetailOrderStatusSearchRespDto;
import com.regent.rbp.task.inno.service.RetailOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author chenchungui
 * @date 2021/9/22
 * @description
 */
@Service
public class RetailOrderServiceImpl implements RetailOrderService {

    private static final String POST_GET_APP_ORDER_LIST = "api/Order/Post_Get_App_Order_Info";

    private static final String POST_GET_ORDER_STATUS_LIST = "api/Order/Get_OrderStatusList";

    @Autowired
    private RetailOrderBillService retailOrderBillService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;
    @Autowired
    private OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    private OnlinePlatformSyncErrorService onlinePlatformSyncErrorService;
    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    /**
     * 拉取订单列表
     *
     * @param param
     */
    @Transactional
    @Override
    public void downloadOnlineOrderList(RetailOrderDownloadOnlineOrderParam param, OnlinePlatform onlinePlatform) throws Exception {
        String key = SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB;
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);

        // 获取销售渠道编号
        String channelCode = baseDbDao.getStringDataBySql(String.format("select code from rbp_channel where id = %s", onlinePlatform.getChannelId()));
        if (map.size() > 0) {
            RetailOrderSearchDto orderSearchDto = new RetailOrderSearchDto();
            orderSearchDto.setOrder_sn_list(StringUtils.join(map.keySet(), ","));
            orderSearchDto.setPageIndex(1);
            this.pullRetailOrderBill(channelCode, param.getOnlinePlatformCode(), onlinePlatform, orderSearchDto, map);
        }

        try {
            RetailOrderSearchDto searchDto = new RetailOrderSearchDto();
            searchDto.setBeginTime(DateUtil.getFullDateStr(param.getBeginTime()));
            searchDto.setEndTime(DateUtil.getFullDateStr(param.getEndTime()));
            searchDto.setOrder_sn_list(param.getOrder_sn_list());
            searchDto.setPageIndex(1);

            this.pullRetailOrderBill(channelCode, param.getOnlinePlatformCode(), onlinePlatform, searchDto, null);

        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, param.getEndTime());
        }
    }

    /**
     * 拉取订单状态
     * @param param
     * @param onlinePlatform
     * @throws Exception
     */
    @Transactional
    @Override
    public void downloadOnlineOrderStatusList(RetailOrderStatusDownloadParam param, OnlinePlatform onlinePlatform) throws Exception {
        String key = SystemConstants.DOWNLOAD_ONLINE_ORDER_STATUS_LIST_JOB;
        // 获取销售渠道编号
        String channelCode = baseDbDao.getStringDataBySql(String.format("select code from rbp_channel where id = %s", onlinePlatform.getChannelId()));
        try {
            RetailOrderSearchDto searchDto = new RetailOrderSearchDto();
            searchDto.setBeginTime(DateUtil.getFullDateStr(param.getBeginTime()));
            searchDto.setEndTime(DateUtil.getFullDateStr(param.getEndTime()));
            searchDto.setOrder_sn_list(param.getOrder_sn_list());
            searchDto.setPageIndex(1);

            this.pullRetailOrderStatusList(channelCode, param.getOnlinePlatformCode(), onlinePlatform, searchDto);

        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, param.getEndTime());
        }
    }

    private void pullRetailOrderStatusList(String channelCode, String onlinePlatformCode, OnlinePlatform onlinePlatform, RetailOrderSearchDto retailOrderSearch) throws Exception {
        RetailOrderSearchReqDto reqDto = new RetailOrderSearchReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(retailOrderSearch);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_GET_ORDER_STATUS_LIST);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        RetailOrderStatusSearchRespDto respDto = JSON.parseObject(result, RetailOrderStatusSearchRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }
        if (CollUtil.isNotEmpty(respDto.getData().getData())) {
            for (RetailOrderStatusDto dto : respDto.getData().getData()) {
                try {
                    // TODO 先只处理确认收货状态
                    this.receiveGoods(dto);
                } catch (Exception ex) {
                    XxlJobHelper.log(String.format("错误信息：%s", ex.getMessage()));
                    ex.printStackTrace();
                    throw ex;
                }
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                retailOrderSearch.setPageIndex(i);
                this.pullRetailOrderStatusList(channelCode, onlinePlatformCode, onlinePlatform, retailOrderSearch);
            }
        }

    }

    /**
     * 确认收货
     *
     * @param retailOrderStatusDto
     */
    private void receiveGoods(RetailOrderStatusDto retailOrderStatusDto) {
        if (null == retailOrderStatusDto) {
            return;
        }
        // 确认收货：orderStatus=1,payStatus=2,shippingStatus=2（此状态为正常订单的最终状态，申请退换货不会改变状态）
        if ("1".equals(retailOrderStatusDto.getOrderStatus())
                || "2".equals(retailOrderStatusDto.getPayStatus())
                || "2".equals(retailOrderStatusDto.getShippingStatus())) {
            RetailOrderBill retailOrderBill = new RetailOrderBill();
            // 6-买家已签收
            retailOrderBill.setOnlineStatus(6);
            retailOrderBill.preUpdate();
            // 更新
            retailOrderBillDao.update(retailOrderBill, new UpdateWrapper<RetailOrderBill>().eq(retailOrderBill.getManualId(), retailOrderStatusDto.getOrderSn()));

            // 线上订单
            Object orderNoList = ThreadLocalGroup.get("yumei_receive_orderno_list");
            Set<String> orderNoList2 = (Set<String>) orderNoList;
            if (null == orderNoList2) {
                orderNoList2 = new HashSet<String>();
            }
            orderNoList2.add(retailOrderStatusDto.getOrderSn());
            ThreadLocalGroup.set("yumei_receive_orderno_list", orderNoList2);

        }

    }

    private void pullRetailOrderBill(String channelCode, String onlinePlatformCode, OnlinePlatform onlinePlatform, RetailOrderSearchDto retailOrderSearch, Map<String, Long> map) throws Exception {
        RetailOrderSearchReqDto reqDto = new RetailOrderSearchReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(retailOrderSearch);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_GET_APP_ORDER_LIST);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        RetailOrderSearchRespDto respDto = JSON.parseObject(result, RetailOrderSearchRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }
        if (respDto.getData().getData().size() > 0) {
            for (RetailOrderMainDto mainDto : respDto.getData().getData()) {
                try {
                    this.saveRetailOrderBill(onlinePlatform.getId(), onlinePlatformCode, channelCode, mainDto, map);
                } catch (Exception ex) {
                    XxlJobHelper.log(String.format("错误信息：%s", ex.getMessage()));
                    ex.printStackTrace();
                    throw ex;
                }
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                retailOrderSearch.setPageIndex(i);
                this.pullRetailOrderBill(channelCode, onlinePlatformCode, onlinePlatform, retailOrderSearch, map);
            }
        }

    }

    private void saveRetailOrderBill(Long onlinePlatformId, String onlinePlatformCode, String channelCode, RetailOrderMainDto mainDto, Map<String, Long> map) {
        RetailOrderBillSaveParam saveParam = new RetailOrderBillSaveParam();
        // 参数转换
        this.convertOrder(mainDto, saveParam);
        saveParam.setOnlinePlatformCode(onlinePlatformCode);
        saveParam.setRetailChannelNo(channelCode);
        // 插入订单
        ModelDataResponse<String> response = retailOrderBillService.save(saveParam);
        // 返回结果
        String orderSn = mainDto.getOrderinfo().getOrder_sn();
        if (map != null && map.containsKey(orderSn)) {
            Long errorId = map.get(orderSn);
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.failure(errorId);
            } else {
                onlinePlatformSyncErrorService.succeed(errorId);
                // 线上订单
                Object orderNoList = ThreadLocalGroup.get("yumei_orderno_list");
                Set<String> orderNoList2 = (Set<String>) orderNoList;
                if (null == orderNoList2) {
                    orderNoList2 = new HashSet<String>();
                }
                orderNoList2.add(orderSn);
                ThreadLocalGroup.set("yumei_orderno_list", orderNoList2);
            }
        } else {
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB, orderSn);
                XxlJobHelper.log(String.format("错误信息：%s %s", orderSn, response.getMessage()));
            } else {
                // 线上订单
                Object orderNoList = ThreadLocalGroup.get("yumei_orderno_list");
                Set<String> orderNoList2 = (Set<String>) orderNoList;
                if (null == orderNoList2) {
                    orderNoList2 = new HashSet<String>();
                }
                orderNoList2.add(orderSn);
                ThreadLocalGroup.set("yumei_orderno_list", orderNoList2);
            }


        }
    }

    /**
     * 参数转换器
     */
    private void convertOrder(RetailOrderMainDto sourceDto, RetailOrderBillSaveParam targetDto) {
        RetailOrderItemDto order = sourceDto.getOrderinfo();
        List<RetailOrderGoodsDto> goods = sourceDto.getOrdergoods();
        List<RetailDstbInfoDto> dstbInfos = sourceDto.getDstbInfo();
        List<RetailOrderBillGoodsDetailData> goodsDetailData = new ArrayList<>();
        List<RetailOrderBillDstbInfo> dstbInfoList = new ArrayList<>();
        targetDto.setGoodsDetailData(goodsDetailData);
        targetDto.setDstbInfo(dstbInfoList);
        /********************** 订单主体 ******************************/
        targetDto.setBillDate(DateUtil.getDate(order.getPay_time(), "yyyy-MM-dd HH:mm:ss"));
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
        targetDto.setPayTime(DateUtil.getDate(order.getPay_time(), "yyyy-MM-dd HH:mm:ss"));
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
        targetDto.setPayCode(order.getPay_code());
        targetDto.setCardNo(order.getCard_num());
        targetDto.setTransactionNo(order.getAlipay_sn());
        targetDto.setAmount(StringUtils.isNotEmpty(order.getOrder_amount()) ? new BigDecimal(order.getOrder_amount()) : BigDecimal.ZERO);
        /********************** 货品明细信息 ******************************/
        if (CollUtil.isNotEmpty(goods)) {
            for (RetailOrderGoodsDto goodsDto : goods) {
                RetailOrderBillGoodsDetailData item = new RetailOrderBillGoodsDetailData();
                goodsDetailData.add(item);
                item.setBarcode(goodsDto.getProduct_sn());
                item.setSaleType(0);
                item.setRetailGoodsType(0);
                item.setTagPrice(new BigDecimal(goodsDto.getMarket_price()));
                item.setBalancePrice(new BigDecimal(goodsDto.getGoods_real_price()));
                item.setDiscount(item.getBalancePrice().divide(new BigDecimal(goodsDto.getMarket_price()), 4, BigDecimal.ROUND_HALF_UP));
                item.setQuantity(new BigDecimal(goodsDto.getGoods_num()));
                item.setRemark(StrUtil.EMPTY);
            }
        }
        /********************** 分销信息 ******************************/
        if (CollUtil.isNotEmpty(dstbInfos)) {
            for (RetailDstbInfoDto info : dstbInfos) {
                RetailOrderBillDstbInfo item = new RetailOrderBillDstbInfo();
                dstbInfoList.add(item);
                item.setLevel(info.getCommLevel());
                item.setDstbCode(info.getDstbStaffCode());
                item.setPhone(info.getDstbStaffPhone());
                item.setMemberCode(info.getDstbCardNum());
                item.setCommType(info.getCommType());
            }
        }
    }


}
