package com.regent.rbp.task.inno.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.service.RetailOrderInnoService;
import com.regent.rbp.task.yumei.model.*;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private RetailOrderBillGoodsDao retailOrderBillGoodsDao;

    @Override
    public Map<String, String> getOrderStatus(String eorderid, String barcode) {
        Map<String, String> response = new HashMap<>();
        Boolean success = null;
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
            this.failure(response, "onlineOrderCodeNotExist");
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
                this.failure(response, "barcodeNotExist");
                return response;
            }
            if (orderBill.getItemStatus().equals(1) || orderBill.getItemStatus().equals(2)) {
                // 退款货品
                List<YumeiOrderItems> items = new ArrayList<>();
                YumeiOrderItems orderItem = new YumeiOrderItems();
                orderItem.setSkuCode(barcode);
                orderItem.setSkuQty(BigDecimal.ONE);
                // orderItem.setRefundAmount(orderBill.getUnitPrice());
                items.add(orderItem);
                success = saleOrderService.orderRefund(order.getStoreNo(), 3, order.getOutOrderNo(), "", items);
                if (success) {
                    // 允许退款
                    this.success(response, order.getOutOrderNo());
                } else {
                    // 不允许
                    this.failure(response, "notAllowedCcancel");

                }
            } else {
                // 不允许
                this.failure(response, "notAllowedCcancel");
            }
        } else {
            // 无条码
            AtomicReference<Boolean> orderStatus = new AtomicReference<>(false);
            order.getOrderItems().stream().forEach(item -> {
                if (item.getItemStatus().equals(1) || item.getItemStatus().equals(2)) {
                    orderStatus.set(true);
                }
            });
            if (orderStatus.get()) {
                // 订单取消
                success = saleOrderService.orderCancel(order.getStoreNo(), 3, order.getOutOrderNo());
                if (success) {
                    // 允许退款
                    this.success(response, order.getOutOrderNo());
                } else {
                    // 不允许
                    this.failure(response, "notAllowedCcancel");
                }
            } else {
                // 不允许
                this.failure(response, "notAllowedCcancel");
            }
        }
        /*// 更新 订单状态
        if (response.get("Flag").equals("1")) {
            this.updOrderByStatus(retailOrderBill.getId(), barcode);
        }*/
        return response;
    }

    private void success(Map<String, String> response, String data) {
        response.put("Flag", "1");
        response.put("Message", LanguageUtil.getMessage("allowedCancel"));
        response.put("data", data);
    }

    private void failure(Map<String, String> response, String messageKey) {
        response.put("Flag", "0");
        response.put("Message", LanguageUtil.getMessage(messageKey));
    }

    /**
     * 更新全渠道订单状态
     * @param id
     * @param barcode
     */
    private void updOrderByStatus(Long id, String barcode) {
        if (null == barcode) {
            List<RetailOrderBillGoods> billGoodsList = retailOrderBillGoodsDao.selectList(new LambdaQueryWrapper<RetailOrderBillGoods>().eq(RetailOrderBillGoods::getBillId, id));
            billGoodsList.stream().forEach(billGoods -> {
                billGoods.preUpdate();
                billGoods.setRefundStatus(4);
                retailOrderBillGoodsDao.updateById(billGoods);
            });
        } else {
            // 查询条码
            Barcode barCode = barcodeDao.selectOne(new LambdaQueryWrapper<Barcode>().eq(Barcode::getBarcode, barcode));
            // 查询订单信息
            RetailOrderBillGoods billGoods = retailOrderBillGoodsDao.selectOne(new LambdaQueryWrapper<RetailOrderBillGoods>()
                    .eq(RetailOrderBillGoods::getBillId, id).eq(RetailOrderBillGoods::getGoodsId, barCode.getGoodsId())
                    .eq(RetailOrderBillGoods::getColorId, barCode.getColorId()).eq(RetailOrderBillGoods::getLongId, barCode.getLongId()).eq(RetailOrderBillGoods::getSizeId, barCode.getSizeId()));

            // 更新全渠道订单状态
            billGoods.preUpdate();
            billGoods.setRefundStatus(4);
            retailOrderBillGoodsDao.updateById(billGoods);
        }
    }

}
