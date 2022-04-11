package com.regent.rbp.task.inno.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.service.RetailOrderInnoService;
import com.regent.rbp.task.yumei.model.*;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: rbp-datacenter
 * @description: 全渠道订单
 * @author: HaiFeng
 * @create: 2022/4/8 9:34
 */
@Service
public class RetailOrderInnoServiceImpl implements RetailOrderInnoService {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Override
    public Map<String, String> getOrderStatus(String eorderid, String barcode) {
        Map<String, String> response = new HashMap<>();

        RetailOrderBill retailOrderBill = retailOrderBillDao.selectOne(new LambdaQueryWrapper<RetailOrderBill>().eq(RetailOrderBill::getManualId, eorderid));
        OrderBusinessPersonDto person = retailOrderBillDao.getOrderBusinessPersonDto(retailOrderBill.getId());

        // 查询玉美订单
        YumeiOrderQueryReq yumeiOrderQueryReq = new YumeiOrderQueryReq();
        yumeiOrderQueryReq.setOutOrderNo(eorderid);
        yumeiOrderQueryReq.setStoreNo(person.getChannelNo());
        yumeiOrderQueryReq.setOrderSource(3);
        yumeiOrderQueryReq.setStartTime(LocalDateTime.parse("2020-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        yumeiOrderQueryReq.setEndTime(LocalDateTime.now());

        YumeiOrderQueryPageResp page = saleOrderService.orderQuery(yumeiOrderQueryReq);
        if (page.getTotal() == 0) {
            // 找不到订单允许取消
            response.put("Flag", "1");
            response.put("Message", LanguageUtil.getMessage("onlineOrderCodeNotExist"));
            response.put("data", eorderid);
            return response;
        }
        // 订单信息
        YumeiOrderQueryPage order = page.getRecords().get(0);
        if (StringUtil.isNotEmpty(barcode)) {
            // 有条码
            // 判断货品明细状态
            YumeiOrderGoods orderBill = order.getOrderItems().stream().filter(f -> f.getSkuCode().equals(barcode)).findAny().orElse(null);
            if (null == orderBill) {
                // 未找到条码信息
                response.put("Flag", "0");
                response.put("Message", LanguageUtil.getMessage("barcodeNotExist"));
                return response;
            }
            if (orderBill.getItemStatus().equals(1) || orderBill.getItemStatus().equals(2)) {
                // 允许退款
                response.put("Flag", "1");
                response.put("Message", LanguageUtil.getMessage("allowedCancel"));

                // 退款货品
                List<YumeiOrderItems> items = new ArrayList<>();
                YumeiOrderItems orderItem = new YumeiOrderItems();
                orderItem.setSkuCode(barcode);
                // orderItem.setRefundAmount(orderBill.getUnitPrice());
                items.add(orderItem);
                saleOrderService.orderRefund(order.getStoreNo(), 3, order.getOutOrderNo(), "", items);
            } else {
                // 不允许
                response.put("Flag", "0");
                response.put("Message", LanguageUtil.getMessage("notAllowedCcancel"));
            }
        } else {
            // 无条码
            AtomicReference<Boolean> orderStatus = new AtomicReference<>(true);
            order.getOrderItems().stream().forEach(item -> {
                if (item.getItemStatus().equals(1) || item.getItemStatus().equals(2)) {
                    orderStatus.set(false);
                }
            });
            if (orderStatus.get()) {
                // 允许退款
                response.put("Flag", "1");
                response.put("Message", LanguageUtil.getMessage("allowedCancel"));

                // 订单取消
                saleOrderService.orderCancel(order.getStoreNo(), 3, order.getOutOrderNo());
            } else {
                // 不允许
                response.put("Flag", "0");
                response.put("Message", LanguageUtil.getMessage("notAllowedCcancel"));
            }
        }

        return response;
    }
}
