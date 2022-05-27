package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderPushLog;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillPushLog;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderPushLogDao;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillPushLogDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.api.dto.retail.RetailOrderInfoDto;
import com.regent.rbp.api.dto.retail.RetalOrderGoodsInfoDto;
import com.regent.rbp.api.dto.sale.SaleOrderQueryParam;
import com.regent.rbp.api.dto.sale.SalesOrderBillGoodsResult;
import com.regent.rbp.api.dto.sale.SalesOrderBillQueryResult;
import com.regent.rbp.api.service.goods.GoodsService;
import com.regent.rbp.api.service.sale.SalesOrderBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.yumei.config.yumei.api.SaleOrderResource;
import com.regent.rbp.task.yumei.constants.YumeiApiUrl;
import com.regent.rbp.task.yumei.model.YumeiCreateSaleOrderDto;
import com.regent.rbp.task.yumei.model.YumeiCredential;
import com.regent.rbp.task.yumei.model.YumeiOfflineSaleOrder;
import com.regent.rbp.task.yumei.model.YumeiOfflineSaleOrderItem;
import com.regent.rbp.task.yumei.model.YumeiOfflineSaleOrderPayload;
import com.regent.rbp.task.yumei.model.YumeiOrder;
import com.regent.rbp.task.yumei.model.YumeiOrderItems;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryPageResp;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryReq;
import com.regent.rbp.task.yumei.model.YumeiRefundItems;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Slf4j
@Data
@Service
public class SaleOrderServiceImpl implements SaleOrderService {
    @Value("${yumei.url:}")
    private String url;

    @Autowired
    private YumeiCredential credential;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Autowired
    private RetailOrderPushLogDao retailOrderPushLogDao;

    @Autowired
    private SaleOrderResource saleOrderResource;
    @Autowired
    private SalesOrderBillService salesOrderBillService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private SalesOrderBillPushLogDao salesOrderBillPushLogDao;

    @Override
    public void confirmReceive(String storeNo, String orderSource, String outOrderNo) {

    }

    /**
     * 推送订单到玉美
     *
     * @param orderNoList 线上单号
     */
    @Transactional
    @Override
    public String pushOrderToYuMei(List<String> orderNoList) {
        List<String> errorMsgList = new ArrayList<>();
        log.info("线上订单号：" + orderNoList.toString());
        List<RetailOrderBill> retailOrderBillList = retailOrderBillDao.selectList(new LambdaQueryWrapper<RetailOrderBill>()
                .in(RetailOrderBill::getManualId, orderNoList));
        if (retailOrderBillList.size() != orderNoList.size()) {
            orderNoList.removeAll(retailOrderBillList.stream().map(RetailOrderBill::getManualId).collect(Collectors.toList()));
            errorMsgList.add("单号不存在" + StrUtil.join(StrUtil.COMMA, orderNoList));
        }

        for (RetailOrderBill retailOrderBill : retailOrderBillList) {
            // 调用成功不再重复调用
            List<RetailOrderPushLog> retailOrderPushLogList = retailOrderPushLogDao.selectList(new LambdaQueryWrapper<RetailOrderPushLog>()
                    .eq(RetailOrderPushLog::getBillNo, retailOrderBill.getManualId())
                    .eq(RetailOrderPushLog::getSucess, 1)
                    .eq(RetailOrderPushLog::getUrl, url + YumeiApiUrl.SALE_ORDER_PUSH));
            if (CollUtil.isNotEmpty(retailOrderPushLogList)) {
                continue;
            }
            OrderBusinessPersonDto orderBusinessPersonDto = this.getOrderBusinessPersonDto(retailOrderBill.getId());
            if (null == orderBusinessPersonDto) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "订单" + retailOrderBill.getBillNo() + "没有分销员或会员没有归属渠道");
            }
            List<YumeiOrder> orderList = new ArrayList<>();
            RetailOrderInfoDto retailOrderInfoDto = retailOrderBillDao.getRetailOrderInfoDto(retailOrderBill.getId());
            YumeiOrder yumeiOrder = new YumeiOrder();
            BeanUtils.copyProperties(retailOrderInfoDto, yumeiOrder);
            List<RetalOrderGoodsInfoDto> retalOrderGoodsInfoDtoList = retailOrderBillDao.getRetalOrderGoodsInfoDto(retailOrderBill.getId());
            if (CollUtil.isNotEmpty(retalOrderGoodsInfoDtoList)) {
                List<YumeiOrder.OrderItem> orderItemList = new ArrayList<>();
                for (RetalOrderGoodsInfoDto retalOrderGoodsInfoDto : retalOrderGoodsInfoDtoList) {
                    YumeiOrder.OrderItem orderItem = new YumeiOrder().new OrderItem();
                    BeanUtils.copyProperties(retalOrderGoodsInfoDto, orderItem);
                    orderItemList.add(orderItem);
                }
                yumeiOrder.setGuideNo(orderBusinessPersonDto.getGuideNo());
                yumeiOrder.setGoodsQty(retalOrderGoodsInfoDtoList.stream().map(RetalOrderGoodsInfoDto::getSkuQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                yumeiOrder.setActualTotalAmount(retalOrderGoodsInfoDtoList.stream().map(RetalOrderGoodsInfoDto::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                yumeiOrder.setOrderItems(orderItemList);
            }
            orderList.add(yumeiOrder);
            // 订单来源（1：美人计会员商城、2：酒会员商城、3：丽晶
            String orderSource = "3";
            String errorMsg = this.pushOrder(orderBusinessPersonDto.getChannelNo(), orderSource, orderList);
            if (StrUtil.isNotEmpty(errorMsg)) {
                errorMsgList.add("订单 " + yumeiOrder.getOutTradeNo() + " 推送失败：" + errorMsg);
            }

            // 更新为已审核状态
            RetailOrderBill updateBill = new RetailOrderBill();
            updateBill.setId(retailOrderBill.getId());
            updateBill.setStatus(1);
            retailOrderBillDao.updateById(updateBill);
        }
        return StrUtil.join(StrUtil.COMMA, errorMsgList);
    }

    @Transactional
    @Override
    public String pushOrderReceiveStatusToYuMei(List<String> orderNoList) {
        List<String> errorMsgList = new ArrayList<>();
        log.info("线上订单号：" + orderNoList.toString());
        List<RetailOrderBill> retailOrderBillList = retailOrderBillDao.selectList(new LambdaQueryWrapper<RetailOrderBill>()
                .in(RetailOrderBill::getManualId, orderNoList));
        if (retailOrderBillList.size() != orderNoList.size()) {
            orderNoList.removeAll(retailOrderBillList.stream().map(RetailOrderBill::getManualId).collect(Collectors.toList()));
            errorMsgList.add("单号不存在" + StrUtil.join(StrUtil.COMMA, orderNoList));
        }

        for (RetailOrderBill retailOrderBill : retailOrderBillList) {
            // 调用成功不再重复调用
            List<RetailOrderPushLog> retailOrderPushLogList = retailOrderPushLogDao.selectList(new LambdaQueryWrapper<RetailOrderPushLog>()
                    .eq(RetailOrderPushLog::getBillNo, retailOrderBill.getManualId())
                    .eq(RetailOrderPushLog::getSucess, 1)
                    .eq(RetailOrderPushLog::getUrl, url + YumeiApiUrl.SALE_ORDER_CONFIRM_RECEIPT));
            if (CollUtil.isNotEmpty(retailOrderPushLogList)) {
                continue;
            }

            OrderBusinessPersonDto orderBusinessPersonDto = this.getOrderBusinessPersonDto(retailOrderBill.getId());
            if (null == orderBusinessPersonDto) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "订单" + retailOrderBill.getBillNo() + "没有分销员或会员没有归属渠道");
            }

            // 订单来源（1：美人计会员商城、2：酒会员商城、3：丽晶
            Integer orderSource = 3;
            String errorMsg = this.orderReceipt(orderBusinessPersonDto.getChannelNo(), orderSource, retailOrderBill.getManualId(), retailOrderBill.getReceivedTime());
            if (StrUtil.isNotEmpty(errorMsg)) {
                errorMsgList.add("订单 " + retailOrderBill.getManualId() + " 确认收货状态推送失败：" + errorMsg);
            }

        }
        return StrUtil.join(StrUtil.COMMA, errorMsgList);
    }

    @Override
    public String pushOrder(String storeNo, String orderSource, List<YumeiOrder> orders) {
        String errorMsg = null;
        if (CollUtil.isEmpty(orders)) {
            return errorMsg;
        }
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("orders", orders);
            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_PUSH);
            log.info("请求参数：" + jsonBody);
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_PUSH)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            log.info("请求结果：" + returnJson);
            String billNo = StrUtil.join(StrUtil.COMMA, orders.stream().map(YumeiOrder::getOutTradeNo).collect(Collectors.toList()));
            RetailOrderPushLog retailOrderPushLog = new RetailOrderPushLog();
            retailOrderPushLog.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            retailOrderPushLog.setBillNo(billNo);
            retailOrderPushLog.setUrl(url + YumeiApiUrl.SALE_ORDER_PUSH);
            retailOrderPushLog.setRequestParam(jsonBody);
            retailOrderPushLog.setResult(returnJson);
            retailOrderPushLog.preInsert();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);
            if (!(Boolean) returnData.getOrDefault("success", false)) {
                retailOrderPushLog.setSucess(0);
                log.error("调用玉美订单推送接口失败" + billNo);
                errorMsg = (String) returnData.get("msg");
            } else {
                retailOrderPushLog.setSucess(1);
                log.info("调用玉美订单推送接口成功" + billNo);
            }
            retailOrderPushLogDao.insert(retailOrderPushLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
        return errorMsg;
    }

    @Override
    public Boolean orderRefund(String storeNo, Integer orderSource, String outOrderNo, String notifyUrl, List<YumeiOrderItems> data) {
        Boolean success = true;
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);
            body.put("orderItems", data);
            body.put("notifyUrl", notifyUrl);
            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_REFUND);
            log.info("请求参数：" + jsonBody);
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_REFUND)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            log.info("请求结果：" + returnJson);

            RetailOrderPushLog retailOrderPushLog = new RetailOrderPushLog();
            retailOrderPushLog.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            retailOrderPushLog.setBillNo(outOrderNo);
            retailOrderPushLog.setUrl(url + YumeiApiUrl.SALE_ORDER_REFUND);
            retailOrderPushLog.setRequestParam(jsonBody);
            retailOrderPushLog.setResult(returnJson);
            retailOrderPushLog.preInsert();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);
            if (!(Boolean) returnData.getOrDefault("success", false)) {
                retailOrderPushLog.setSucess(0);
                log.error("调用玉美订单退货接口失败" + outOrderNo);
                success = false;
            } else {
                retailOrderPushLog.setSucess(1);
                log.info("调用玉美订单退货接口成功" + outOrderNo);
                success = true;
            }
            retailOrderPushLogDao.insert(retailOrderPushLog);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public String orderReceipt(String storeNo, Integer orderSource, String outOrderNo, String confirmTime) {
        String errorMsg = null;
        if (StrUtil.isEmpty(outOrderNo)) {
            return errorMsg;
        }
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);
            body.put("confirmTime", confirmTime);
            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_CONFIRM_RECEIPT);
            log.info("请求参数：" + jsonBody);
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_CONFIRM_RECEIPT)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            log.info("请求结果：" + returnJson);

            RetailOrderPushLog retailOrderPushLog = new RetailOrderPushLog();
            retailOrderPushLog.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            retailOrderPushLog.setBillNo(outOrderNo);
            retailOrderPushLog.setUrl(url + YumeiApiUrl.SALE_ORDER_CONFIRM_RECEIPT);
            retailOrderPushLog.setRequestParam(jsonBody);
            retailOrderPushLog.setResult(returnJson);
            retailOrderPushLog.preInsert();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);
            if (!(Boolean) returnData.getOrDefault("success", false)) {
                retailOrderPushLog.setSucess(0);
                log.error("调用玉美订单推送接口失败" + outOrderNo);
                errorMsg = (String) returnData.get("msg");
            } else {
                retailOrderPushLog.setSucess(1);
                log.info("调用玉美订单推送接口成功" + outOrderNo);
            }
            retailOrderPushLogDao.insert(retailOrderPushLog);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
        return errorMsg;
    }

    /**
     * 订单查询接口
     *
     * @param query
     * @return
     */
    @Override
    public YumeiOrderQueryPageResp orderQuery(YumeiOrderQueryReq query) {
        if (null == query) {
            throw new BusinessException(ResponseCode.PARAMS_EMPTY);
        }

        if (StringUtils.isEmpty(query.getStoreNo())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotNull", new Object[]{"门店编号"});
        }
        if (null == query.getOrderSource()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotNull", new Object[]{"订单来源"});
        }
        if (null == query.getStartTime()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotNull", new Object[]{"开始时间"});
        }
        if (null == query.getEndTime()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotNull", new Object[]{"结束时间"});
        }

        YumeiOrderQueryPageResp resp = null;
        try {
            HashMap<String, Object> body = new HashMap<>();
            body.put("storeNo", query.getStoreNo());
            body.put("orderSource", query.getOrderSource());
            body.put("status", query.getStatus());
            body.put("startTime", query.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            body.put("endTime", query.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            body.put("outOrderNo", query.getOutOrderNo());
            body.put("pageNum", Optional.ofNullable(query.getPageNum()).orElse(1));
            body.put("pageSize", Optional.ofNullable(query.getPageSize()).orElse(10));

            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_QUERY);
            log.info("请求参数：" + jsonBody);
            // 查询
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_QUERY)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            Map<String, Object> resultMap = objectMapper.readValue(returnJson, Map.class);
            log.info("请求结果：" + returnJson);
            if (null == resultMap || null == resultMap.get("code")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{"返回结果"});
            }
            if (!resultMap.get("code").equals("00000")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramVerifyError", new Object[]{String.format("requestId:%s, msg:%s", resultMap.get("requestId"), resultMap.get("msg"))});
            }
            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) resultMap.get("data");
            resp = objectMapper.readValue(JSONObject.toJSONString(data), YumeiOrderQueryPageResp.class);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("订单查询接口发生异常:{}", e.getMessage());
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, "paramVerifyError", new Object[]{e.getMessage()});
        }
        return resp;
    }

    @Override
    public Boolean orderCancel(String storeNo, Integer orderSource, String outOrderNo) {
        Boolean success = true;
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);

            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_CANCEL);
            log.info("请求参数：" + jsonBody);
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_CANCEL)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            log.info("请求结果：" + returnJson);

            RetailOrderPushLog retailOrderPushLog = new RetailOrderPushLog();
            retailOrderPushLog.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            retailOrderPushLog.setBillNo(outOrderNo);
            retailOrderPushLog.setUrl(url + YumeiApiUrl.SALE_ORDER_CANCEL);
            retailOrderPushLog.setRequestParam(jsonBody);
            retailOrderPushLog.setResult(returnJson);
            retailOrderPushLog.preInsert();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);
            if (!(Boolean) returnData.getOrDefault("success", false)) {
                retailOrderPushLog.setSucess(0);
                log.error("调用玉美销售订单_取消接口失败" + outOrderNo);
                success = false;
            } else {
                retailOrderPushLog.setSucess(1);
                log.info("调用玉美销售订单_取消接口成功" + outOrderNo);
            }
            retailOrderPushLogDao.insert(retailOrderPushLog);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public OrderBusinessPersonDto getOrderBusinessPersonDto(Long retailOrderBillId) {
        if (null == retailOrderBillId) {
            return null;
        }
        // 取分销员所属店铺
        OrderBusinessPersonDto orderBusinessPersonDto = retailOrderBillDao.getOrderBusinessPersonDto(retailOrderBillId);
        if (null == orderBusinessPersonDto) {
            // 没有分销员，取会员所属店铺
            orderBusinessPersonDto = retailOrderBillDao.getMemberCardChannel(retailOrderBillId);
        }
        return orderBusinessPersonDto;
    }

    @Override
    public String pushRefundLogistics(String storeNo, String orderSource, String outOrderNo, List<YumeiRefundItems> orderItems) {
        String errorMsg = null;
        if (CollUtil.isEmpty(orderItems)) {
            return errorMsg;
        }
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);
            body.put("orderItems", orderItems);
            String jsonBody = objectMapper.writeValueAsString(body);
            log.info("请求url：" + url + YumeiApiUrl.SALE_ORDER_PUSH_REFUND);
            log.info("请求参数：" + jsonBody);
            String returnJson = HttpUtil.createRequest(Method.POST, url + YumeiApiUrl.SALE_ORDER_PUSH_REFUND)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            log.info("请求结果：" + returnJson);

            RetailOrderPushLog retailOrderPushLog = new RetailOrderPushLog();
            retailOrderPushLog.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            retailOrderPushLog.setBillNo(outOrderNo);
            retailOrderPushLog.setUrl(url + YumeiApiUrl.SALE_ORDER_PUSH_REFUND);
            retailOrderPushLog.setRequestParam(jsonBody);
            retailOrderPushLog.setResult(returnJson);
            retailOrderPushLog.preInsert();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);
            if (!(Boolean) returnData.getOrDefault("success", false)) {
                retailOrderPushLog.setSucess(0);
                log.error("调用玉美销售订单_退货物流上传接口失败" + outOrderNo);
                errorMsg = (String) returnData.get("msg");
            } else {
                retailOrderPushLog.setSucess(1);
                log.info("调用玉美销售订单_退货物流上传接口成功" + outOrderNo);
            }
            retailOrderPushLogDao.insert(retailOrderPushLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
        return errorMsg;
    }

    @Override
    @Transactional
    public String createOfflineSaleOrder(String billNo) {
        String strMsg = StringUtil.EMPTY;
        SaleOrderQueryParam param = new SaleOrderQueryParam();
        param.setBillNo(billNo);
        try {
            PageDataResponse<SalesOrderBillQueryResult> query = salesOrderBillService.query(param);
            if (CollUtil.isNotEmpty(query.getData())) {
                SalesOrderBillQueryResult bill = query.getData().get(0);

                YumeiOfflineSaleOrderPayload payload = new YumeiOfflineSaleOrderPayload();
                payload.setStoreNo(bill.getChannelCode());
                ArrayList<YumeiOfflineSaleOrder> orders = new ArrayList<>();
                YumeiOfflineSaleOrder order = new YumeiOfflineSaleOrder();
                order.setOutTradeNo(bill.getBillNo());
                order.setUserRemark(bill.getNotes());
                order.setPayTime(bill.getCreatedTime());

                ArrayList<YumeiOfflineSaleOrderItem> orderItems = new ArrayList<>();

                List<Long> goodsId = CollUtil.distinct(CollUtil.map(bill.getGoodsDetailData(), SalesOrderBillGoodsResult::getGoodsId, true));
                Map<Long, Map<Long, Barcode>> barcodeMap = barcodeDao.selectList(Wrappers.lambdaQuery(Barcode.class).in(Barcode::getGoodsId, goodsId)).stream().collect(Collectors.groupingBy(Barcode::getGoodsId, Collectors.collectingAndThen(Collectors.toMap(Barcode::getId, Function.identity()), Collections::unmodifiableMap)));

                bill.getGoodsDetailData().forEach(gd -> {
                    YumeiOfflineSaleOrderItem orderItem = new YumeiOfflineSaleOrderItem();
                    orderItem.setGoodsName(gd.getGoodsName());
                    Map<Long, Barcode> barcodeCandidate = barcodeMap.get(gd.getGoodsId());
                    if (gd.getBarcodeId() != null) {
                        orderItem.setSkuCode(barcodeCandidate.get(gd.getBarcodeId()).getBarcode());
                    } else {
                        orderItem.setSkuCode(barcodeCandidate.values().stream().findFirst().get().getBarcode());
                    }
                    orderItem.setSkuQty(gd.getQuantity());
                    orderItem.setUnitPrice(gd.getBalancePrice());
                    orderItem.setOutRefundNo(bill.getOriginBillNo());
                    orderItems.add(orderItem);
                });
                order.setOrderItems(orderItems);
                payload.setOrders(orders);
                String jsonBody = objectMapper.writeValueAsString(payload);
                YumeiCreateSaleOrderDto dto = saleOrderResource.createOfflineSaleOrder(payload);

                if (dto.getCode().equals("00000")) {
                    String returnJson = objectMapper.writeValueAsString(dto);
                    // 写入成功记录
                    SalesOrderBillPushLog log = new SalesOrderBillPushLog(bill.getId(), bill.getBillNo(), "", jsonBody, returnJson, 1);
                    salesOrderBillPushLogDao.insert(log);
                } else {
                    strMsg = dto.getSubMsg();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
        return strMsg;
    }
}
