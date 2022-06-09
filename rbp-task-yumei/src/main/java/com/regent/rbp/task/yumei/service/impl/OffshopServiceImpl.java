package com.regent.rbp.task.yumei.service.impl;


import com.regent.rbp.api.dao.warehouse.user.UserProfileDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.api.service.send.SendBillService;
import com.regent.rbp.task.yumei.config.yumei.api.OffShopResource;
import com.regent.rbp.task.yumei.model.*;
import com.regent.rbp.task.yumei.service.OffshopService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffshopServiceImpl implements OffshopService {
    @Autowired
    private SendBillService sendBillService;
    @Autowired
    private OffShopResource offShopResource;
    @ApiModelProperty
    private ReceiveBillService receiveBillService;
    @Autowired
    private UserProfileDao userProfileDao;

    @Override
    public void createReturnOrder(String billNo,AuditData auditData) {
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (!query.getData().isEmpty()) {
            SendBillQueryResult bill = query.getData().get(0);
            YumeiReturnOrderCreatePayload payload = new YumeiReturnOrderCreatePayload();
            payload.setStoreNo(bill.getChannelCode());

            ArrayList<YumeiReturnOrder> orders = new ArrayList<>();
            YumeiReturnOrder order = new YumeiReturnOrder();
            order.setOutOrderNo(bill.getBillNo());
            order.setBasicOffshopCode(bill.getChannelCode());
            order.setBasicOffshopName(bill.getChannelName());
            order.setBasicWarehouseCode(bill.getToChannelCode());
            order.setBasicWarehouseName(bill.getToChannelName());
            order.setApplyUser(bill.getCreatedByName());
            order.setReviewUser(userProfileDao.selectById(auditData.getCheckBy()).getName());
            order.setLogistics(bill.getLogisticsCompanyName());
            order.setLogisticsCode(bill.getLogisticsCompanyCode());
            order.setLogisticsNo(bill.getLogisticsBillCode());
            order.setCreateTime(new Date());

            List<OrderDetail> orderItems = bill.getGoodsDetailData().stream().map(e -> {
                OrderDetail item = new OrderDetail();
                item.setGoodsName(e.getGoodsName());
                item.setGoodsNo(e.getGoodsCode());
                item.setSkuCode(e.getBarcode());
                item.setQty(e.getQuantity());
                item.setCostPrice(e.getBalancePrice());
                return item;
            }).collect(Collectors.toList());
            order.setOrderDetails(orderItems);
            orders.add(order);
            payload.setOrders(orders);
            offShopResource.returnOrderCreate(payload);
        }
    }

    @Override
    public void cancelReturnOrder(String billNo) {
        YumeiReturnOrderValidatedPayload payload = new YumeiReturnOrderValidatedPayload();
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (query.getData().isEmpty()) {
            SendBillQueryResult bill = query.getData().get(0);
            payload.setStoreNo(bill.getChannelCode());
            payload.setOutOrderNo(bill.getBillNo());
            offShopResource.returnOrderValidated(payload);
        }
    }

    @Override
    public void checkChannelTuneOut(String billNo) {
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (query.getData().isEmpty()) {
            SendBillQueryResult bill = query.getData().get(0);

            YumeiTransferOrderCreatePayload payload = new YumeiTransferOrderCreatePayload();
            payload.setTransferType("out");
            List<YumeiTransferOrderCreate> orders = new ArrayList<>();
            YumeiTransferOrderCreate order = new YumeiTransferOrderCreate();
            order.setTransferOrderNo(null);//todo 上级单据 send_bill_goods
            order.setOutOrderNo(billNo);
            order.setBasicInOffshopCode(bill.getToChannelCode());
            order.setBasicInOffshopName(bill.getChannelName());
            order.setBasicOutOffshopCode(bill.getChannelCode());
            order.setBasicOutOffshopName(bill.getChannelName());
            order.setApplyUser(bill.getCheckByName());
            order.setLogistics(bill.getLogisticsCompanyName());
            order.setLogisticsCode(bill.getLogisticsCompanyCode());
            order.setLogisticsNo(bill.getLogisticsBillCode());
            order.setCreateTime(bill.getCreatedTime());
            order.setDeliveryTime(null); //todo
            order.setReceiveTime(null); //todo

            List<OrderDetail> orderItems = bill.getGoodsDetailData().stream().map(e -> {
                OrderDetail item = new OrderDetail();
                item.setGoodsName(e.getGoodsName());
                item.setGoodsNo(e.getGoodsCode());
                item.setSkuCode(e.getBarcode());
                item.setQty(e.getQuantity());
                item.setCostPrice(e.getBalancePrice());
                return item;
            }).collect(Collectors.toList());
            order.setStockOutQty(order.getApplyQty());
            order.setStockOutAmount(order.getTotalCost());
            order.setOrderDetails(orderItems);

            orders.add(order);
            payload.setOrders(orders);
            offShopResource.transferOrderCreate(payload);
        }
    }

    @Override
    public void checkChannelTuneIn(String billNo) {
        ReceiveBillQueryParam param = new ReceiveBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<ReceiveBillQueryResult> query = receiveBillService.query(param);
        if (query.getData().isEmpty()) {
            ReceiveBillQueryResult bill = query.getData().get(0);

            SendBillQueryParam sendBillQueryParam = new SendBillQueryParam();
            sendBillQueryParam.setBillNo(billNo);
            SendBillQueryResult sendBill = sendBillService.query(sendBillQueryParam).getData().get(0);
            YumeiTransferOrderCreatePayload payload = new YumeiTransferOrderCreatePayload();
            payload.setTransferType("in");
            List<YumeiTransferOrderCreate> orders = new ArrayList<>();
            YumeiTransferOrderCreate order = new YumeiTransferOrderCreate();
            order.setTransferOrderNo(bill.getSendNo());
            order.setOutOrderNo(billNo);
            order.setBasicInOffshopCode(bill.getToChannelCode());
            order.setBasicInOffshopName(bill.getChannelName());
            order.setBasicOutOffshopCode(bill.getChannelCode());
            order.setBasicOutOffshopName(bill.getChannelName());
            order.setApplyUser(bill.getCheckByName());
            order.setLogistics(sendBill.getLogisticsCompanyName());
            order.setLogisticsCode(sendBill.getLogisticsCompanyCode());
            order.setLogisticsNo(sendBill.getLogisticsBillCode());
            order.setCreateTime(bill.getCreatedTime());
            order.setDeliveryTime(null); //todo
            order.setReceiveTime(null); //todo

            List<OrderDetail> orderItems = bill.getGoodsDetailData().stream().map(e -> {
                OrderDetail item = new OrderDetail();
                item.setGoodsName(e.getGoodsName());
                item.setGoodsNo(e.getGoodsCode());
                item.setSkuCode(e.getBarcode());
                item.setQty(e.getQuantity());
                item.setCostPrice(e.getBalancePrice());
                return item;
            }).collect(Collectors.toList());
            order.setStockInQty(order.getApplyQty());
            order.setStockInAmount(order.getTotalCost());
            order.setOrderDetails(orderItems);

            orders.add(order);
            payload.setOrders(orders);
            offShopResource.transferOrderCreate(payload);
        }
    }
}
