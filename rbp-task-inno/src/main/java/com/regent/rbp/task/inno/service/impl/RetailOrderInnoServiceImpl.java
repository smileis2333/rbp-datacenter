package com.regent.rbp.task.inno.service.impl;

import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.task.inno.service.OnlineSyncGoodsStockService;
import com.regent.rbp.task.inno.service.RetailOrderInnoService;
import com.regent.rbp.task.yumei.model.*;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, String> getOrderStatus(String eorderid, String barcode) {
        Map<String, String> response = new HashMap<>();

        // 查询玉美订单
        YumeiOrderQueryReq yumeiOrderQueryReq = new YumeiOrderQueryReq();
        yumeiOrderQueryReq.setOutOrderNo(eorderid);
        YumeiOrderQueryPageResp page = saleOrderService.orderQuery(yumeiOrderQueryReq);
        if (page.getTotalCount() == 0) {
            // 找不到订单允许取消
            response.put("Flag", "1");
            response.put("Message", LanguageUtil.getMessage("onlineOrderCodeNotExist"));
            response.put("data", eorderid);
            return response;
        }
        // 订单信息
        YumeiOrderQueryPage order = page.getOrders().get(0);
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
            orderItem.setRefundAmount(orderBill.getUnitPrice());
            items.add(orderItem);
            saleOrderService.orderRefund(order.getStoreNo(), order.getOrderSource(), order.getOutOrderNo(), "", items);
        } else {
            // 不允许
            response.put("Flag", "0");
            response.put("Message", LanguageUtil.getMessage("notAllowedCcancel"));
        }
        return response;
    }
}
