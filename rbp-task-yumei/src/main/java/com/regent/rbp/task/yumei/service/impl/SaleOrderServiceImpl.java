package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.api.dto.retail.RetailOrderInfoDto;
import com.regent.rbp.api.dto.retail.RetalOrderGoodsInfoDto;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.task.yumei.constants.YumeiApiUrl;
import com.regent.rbp.task.yumei.model.YumeiCredential;
import com.regent.rbp.task.yumei.model.YumeiOrder;
import com.regent.rbp.task.yumei.model.YumeiOrderItems;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryPageResp;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryReq;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Log4j2
@Data
@Service
public class SaleOrderServiceImpl implements SaleOrderService {
    @Autowired
    private YumeiCredential credential;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Override
    public void confirmReceive(String storeNo, String orderSource, String outOrderNo) {

    }

    @Transactional
    public void pushOrderToYuMei(List<String> orderNoList) {
        List<RetailOrderBill> retailOrderBillList = retailOrderBillDao.selectList(new LambdaQueryWrapper<RetailOrderBill>()
                .in(RetailOrderBill::getBillNo, orderNoList));
        for (RetailOrderBill retailOrderBill : retailOrderBillList) {
            OrderBusinessPersonDto orderBusinessPersonDto = retailOrderBillDao.getOrderBusinessPersonDto(retailOrderBill.getId());
            if (null == orderBusinessPersonDto) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "订单" + retailOrderBill.getBillNo() + "没有分销员");
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
                yumeiOrder.setGoodsQty(retalOrderGoodsInfoDtoList.stream().map(RetalOrderGoodsInfoDto::getSkuQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                yumeiOrder.setActualTotalAmount(retalOrderGoodsInfoDtoList.stream().map(RetalOrderGoodsInfoDto::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                yumeiOrder.setOrderItems(orderItemList);
            }
            // 订单来源（1：美人计会员商城、2：酒会员商城、3：丽晶
            String orderSource = "3";
            this.pushOrder(orderBusinessPersonDto.getChannelNo(), orderSource, orderList);
        }
    }

    @Override
    public void pushOrder(String storeNo, String orderSource, List<YumeiOrder> orders) {
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("orders", orders);
            String jsonBody = objectMapper.writeValueAsString(body);
            String returnJson = HttpUtil.createRequest(Method.POST, YumeiApiUrl.SALE_ORDER_PUSH)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
    }

    @Override
    public void orderRefund(String storeNo, Integer orderSource, String outOrderNo, String notifyUrl, List<YumeiOrderItems> data) {
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);
            body.put("orderItems", data);
            body.put("notifyUrl", notifyUrl);

            String jsonBody = objectMapper.writeValueAsString(body);
            String returnJson = HttpUtil.createRequest(Method.POST, YumeiApiUrl.SALE_ORDER_REFUND)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN",credential.getAccessToken())
                    .execute()
                    .body();
            Map<String,Object> returnData = (Map<String,Object>)objectMapper.readValue(returnJson, Map.class);
            if (!returnData.get("code").equals("TFS00000")) {
                throw new Exception(returnData.get("msg").toString());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void orderReceipt(String storeNo, Integer orderSource, String outOrderNo) {
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("outOrderNo", outOrderNo);

            String jsonBody = objectMapper.writeValueAsString(body);
            String returnJson = HttpUtil.createRequest(Method.POST, YumeiApiUrl.SALE_ORDER_CONFIRM_RECEIPT)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN",credential.getAccessToken())
                    .execute()
                    .body();
            Map<String,Object> returnData = (Map<String,Object>)objectMapper.readValue(returnJson, Map.class);
            if (!returnData.get("code").equals("TRS00000")) {
                throw new Exception(returnData.get("msg").toString());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (StringUtils.isEmpty(query.getOutOrderNo())) {
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
        }

        YumeiOrderQueryPageResp resp = null;
        try {
            HashMap<String, Object> body = new HashMap<>();
            body.put("storeNo", query.getStoreNo());
            body.put("orderSource", query.getOrderSource());
            body.put("status", query.getStatus());
            body.put("startTime", query.getStartTime());
            body.put("endTime", query.getEndTime());
            body.put("outOrderNo", query.getOutOrderNo());
            body.put("pageNum", Optional.ofNullable(query.getPageNum()).orElse(1));
            body.put("pageSize", Optional.ofNullable(query.getPageSize()).orElse(10));
            // 查询
            String returnJson = HttpUtil.createRequest(Method.POST, YumeiApiUrl.SALE_ORDER_QUERY)
                    .body(objectMapper.writeValueAsString(body))
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            Map<String, String> resultMap = objectMapper.readValue(returnJson, Map.class);
            if (null == resultMap || null == resultMap.get("code")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{"返回结果"});
            }
            if (!resultMap.get("code").equals("1")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramVerifyError", new Object[]{String.format("requestId:%s, msg:%s", resultMap.get("requestId"), resultMap.get("msg"))});
            }
            resp = objectMapper.readValue(resultMap.get("data"), YumeiOrderQueryPageResp.class);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("订单查询接口发生异常:{}", e.getMessage());
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, "paramVerifyError", new Object[]{e.getMessage()});
        }
        return resp;
    }

}
