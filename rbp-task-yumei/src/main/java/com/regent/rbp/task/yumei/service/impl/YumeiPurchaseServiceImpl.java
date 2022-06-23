package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.sendBill.SendBillDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.task.yumei.config.yumei.api.PurchaseResource;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveBillOrder;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveBillOrderItem;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveOrderPayload;
import com.regent.rbp.task.yumei.service.YumeiPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/26
 * @description
 */
@Service
public class YumeiPurchaseServiceImpl implements YumeiPurchaseService {
    @Autowired
    private ReceiveBillService receiveBillService;
    @Autowired
    private PurchaseResource purchaseResource;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SendBillDao sendBillDao;


    @Override
    public void createPurchaseReceive(String billNo) {
        ReceiveBillQueryParam param = new ReceiveBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<ReceiveBillQueryResult> query = receiveBillService.query(param);
        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }

        ReceiveBillQueryResult bill = query.getData().get(0);
        YumeiPurchaseReceiveOrderPayload payload = new YumeiPurchaseReceiveOrderPayload();
        payload.setStoreNo(bill.getToChannelCode());
        ArrayList<YumeiPurchaseReceiveBillOrder> orders = new ArrayList<>();
        YumeiPurchaseReceiveBillOrder order = new YumeiPurchaseReceiveBillOrder();
        order.setOutOrderNo(billNo);

        SendBill sendBill = sendBillDao.selectOne(Wrappers.lambdaQuery(SendBill.class).eq(SendBill::getBillNo, bill.getSendNo()));
        order.setDeliveryOrderNo(sendBill.getManualId());
        order.setBasicOffshopCode(bill.getToChannelCode());
        order.setBasicOffshopName(bill.getToChannelName());
        order.setPoInTime(bill.getCreatedTime());
        List<YumeiPurchaseReceiveBillOrderItem> orderItems = new ArrayList<>();

        bill.getGoodsDetailData().forEach(gd -> {
            YumeiPurchaseReceiveBillOrderItem orderItem = new YumeiPurchaseReceiveBillOrderItem();
            orderItem.setGoodsName(gd.getGoodsName());
            orderItem.setGoodsNo(gd.getGoodsCode());
            orderItem.setSkuCode(gd.getBarcode());
            orderItem.setPoInQty(gd.getPlanQuantity());
            orderItem.setActualPoInQty(gd.getQuantity());
            orderItem.setTaxIncludedPurchaseUnitPrice(gd.getBalancePrice());
            orderItem.setExcludingTaxPurchaseUnitPrice(gd.getBalancePrice());
            orderItems.add(orderItem);
        });
        order.setOrderDetails(orderItems);
        orders.add(order);
        payload.setOrders(orders);
        purchaseResource.createPurchaseReceive(payload);
    }
}
