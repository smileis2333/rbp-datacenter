package com.regent.rbp.task.yumei.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.regent.rbp.api.core.eum.OnlinePlatformTypeEnum;
import com.regent.rbp.api.core.retail.LogisticsCompanyPlatformMapping;
import com.regent.rbp.api.dao.warehouse.user.UserProfileDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.dto.stock.StockAdjustBillGoodsDetailData;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryParam;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryResult;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.api.service.retail.LogisticsCompanyPlatformMappingService;
import com.regent.rbp.api.service.send.SendBillService;
import com.regent.rbp.api.service.stock.StockAdjustBillService;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.task.yumei.config.yumei.api.OffShopResource;
import com.regent.rbp.task.yumei.model.AuditData;
import com.regent.rbp.task.yumei.model.CreateOtherStockOrder;
import com.regent.rbp.task.yumei.model.CreateOtherStockPayload;
import com.regent.rbp.task.yumei.model.OrderDetail;
import com.regent.rbp.task.yumei.model.SimpleOrderDetail;
import com.regent.rbp.task.yumei.model.YumeiReturnOrder;
import com.regent.rbp.task.yumei.model.YumeiReturnOrderCreatePayload;
import com.regent.rbp.task.yumei.model.YumeiReturnOrderValidatedPayload;
import com.regent.rbp.task.yumei.model.YumeiTransferOrderCreate;
import com.regent.rbp.task.yumei.model.YumeiTransferOrderCreatePayload;
import com.regent.rbp.task.yumei.service.OffshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OffshopServiceImpl implements OffshopService {
    @Autowired
    private SendBillService sendBillService;
    @Autowired
    private OffShopResource offShopResource;
    @Autowired
    private ReceiveBillService receiveBillService;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private LogisticsCompanyPlatformMappingService logisticsCompanyPlatformMappingService;
    @Autowired
    private DbDao dbDao;
    @Autowired
    private StockAdjustBillService stockAdjustBillService;
    @Autowired
    private YumeiPushService yumeiPushService;

    @Override
    public void createReturnOrder(String billNo, AuditData auditData) {
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }
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
        LogisticsCompanyPlatformMapping logisticCompany = logisticsCompanyPlatformMappingService.getOnlinePlatformLogisticsCode(bill.getLogisticsCompanyCode(), OnlinePlatformTypeEnum.INNO.getId());
        order.setLogisticsCode(logisticCompany.getOnlinePlatformLogisticsCode());
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
        Date startTime = new Date();
        offShopResource.returnOrderCreate(payload);
        Date endTime = new Date();
        String insertStatement = String.format("" +
                "insert\n" +
                "\tinto\n" +
                "\tyumei_push_log(\n" +
                "\tbill_id,\n" +
                "\tbill_no,\n" +
                "\ttarget_table,\n" +
                "\tpush_start_time,\n" +
                "\tpush_end_time)\n" +
                "values(%s,'%s','%s','%s','%s')", bill.getBillId(), billNo, TableConstants.SEND_BILL, DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
        dbDao.insert(insertStatement);
    }

    @Override
    public void cancelReturnOrder(String billNo) {
        YumeiReturnOrderValidatedPayload payload = new YumeiReturnOrderValidatedPayload();
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }
        SendBillQueryResult bill = query.getData().get(0);
        payload.setStoreNo(bill.getChannelCode());
        payload.setOutOrderNo(bill.getBillNo());
        offShopResource.returnOrderValidated(payload);
    }

    @Override
    public void checkChannelTuneOut(String billNo) {
        SendBillQueryParam param = new SendBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<SendBillQueryResult> query = sendBillService.query(param);
        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }

        SendBillQueryResult bill = query.getData().get(0);

        YumeiTransferOrderCreatePayload payload = new YumeiTransferOrderCreatePayload();
        payload.setTransferType(2);
        List<YumeiTransferOrderCreate> orders = new ArrayList<>();
        YumeiTransferOrderCreate order = new YumeiTransferOrderCreate();
        order.setTransferOrderNo(bill.getGoodsDetailData().get(0).getNoticeNo());
        order.setOutOrderNo(billNo);
        order.setBasicInOffshopCode(bill.getToChannelCode());
        order.setBasicInOffshopName(bill.getToChannelName());
        order.setBasicOutOffshopCode(bill.getChannelCode());
        order.setBasicOutOffshopName(bill.getChannelName());
        order.setApplyUser(bill.getCheckByName());
        order.setLogistics(bill.getLogisticsCompanyName());
        order.setLogisticsCode(bill.getLogisticsCompanyCode());
        order.setLogisticsNo(bill.getLogisticsBillCode());
        order.setCreateTime(bill.getCreatedTime());
        order.setDeliveryTime(bill.getCheckTime());

        List<SimpleOrderDetail> orderItems = bill.getGoodsDetailData().stream().map(e -> {
            SimpleOrderDetail item = new SimpleOrderDetail();
            item.setGoodsName(e.getGoodsName());
            item.setGoodsNo(e.getGoodsCode());
            item.setSkuCode(e.getBarcode());
            item.setQty(e.getQuantity());
            return item;
        }).collect(Collectors.toList());
        order.setOrderDetails(orderItems);

        orders.add(order);
        payload.setOrders(orders);
        offShopResource.transferOrderCreate(payload);
    }

    @Override
    public void checkChannelTuneIn(String billNo) {
        ReceiveBillQueryParam param = new ReceiveBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<ReceiveBillQueryResult> query = receiveBillService.query(param);
        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }

        ReceiveBillQueryResult bill = query.getData().get(0);

        if (!yumeiPushService.ifPush(bill.getSendNo())) {
            String msg = String.format("skip push %s channel tune in, because of tune out wait to push first", billNo);
            log.info(msg);
            throw new BusinessException(ResponseCode.PARAMS_ERROR, msg);
        }

        SendBillQueryParam sendBillQueryParam = new SendBillQueryParam();
        sendBillQueryParam.setBillNo(bill.getSendNo());
        SendBillQueryResult sendBill = sendBillService.query(sendBillQueryParam).getData().get(0);
        YumeiTransferOrderCreatePayload payload = new YumeiTransferOrderCreatePayload();
        payload.setTransferType(1);
        List<YumeiTransferOrderCreate> orders = new ArrayList<>();
        YumeiTransferOrderCreate order = new YumeiTransferOrderCreate();
        order.setTransferOrderNo(bill.getSendNo());
        order.setOutOrderNo(billNo);
        order.setBasicInOffshopCode(bill.getToChannelCode());
        order.setBasicInOffshopName(bill.getToChannelName());
        order.setBasicOutOffshopCode(bill.getChannelCode());
        order.setBasicOutOffshopName(bill.getChannelName());
        order.setApplyUser(bill.getCheckByName());
        order.setLogistics(sendBill.getLogisticsCompanyName());
        order.setLogisticsCode(sendBill.getLogisticsCompanyCode());
        order.setLogisticsNo(sendBill.getLogisticsBillCode());
        order.setCreateTime(bill.getCreatedTime());
        order.setDeliveryTime(sendBill.getCheckTime());
        order.setReceiveTime(bill.getCheckTime());

        List<SimpleOrderDetail> orderItems = bill.getGoodsDetailData().stream().map(e -> {
            SimpleOrderDetail item = new SimpleOrderDetail();
            item.setGoodsName(e.getGoodsName());
            item.setGoodsNo(e.getGoodsCode());
            item.setSkuCode(e.getBarcode());
            item.setQty(e.getQuantity());
            return item;
        }).collect(Collectors.toList());
        order.setOrderDetails(orderItems);

        orders.add(order);
        payload.setOrders(orders);
        offShopResource.transferOrderCreate(payload);
    }

    @Override
    public void checkStockAdjust(String billNo) {
        StockAdjustBillQueryParam param = new StockAdjustBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<StockAdjustBillQueryResult> query = stockAdjustBillService.query(param);

        if (CollUtil.isEmpty(query.getData())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, String.format("billNo: %s 不存在", billNo));
        }

        StockAdjustBillQueryResult bill = query.getData().get(0);

        CreateOtherStockPayload payload = new CreateOtherStockPayload();
        CreateOtherStockOrder order = new CreateOtherStockOrder();

        payload.setStoreNo(bill.getChannelCode());
        payload.setReason(bill.getBusinessType());

        order.setOutOrderNo(billNo);

        List<OrderDetail> orderDetails = new ArrayList<>();
        order.setOrderDetails(orderDetails);

        for (StockAdjustBillGoodsDetailData goodsDetail : bill.getGoodsDetailData()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGoodsName(goodsDetail.getGoodsName());
            orderDetail.setGoodsNo(goodsDetail.getGoodsCode());
            orderDetail.setSkuCode(goodsDetail.getBarcode());
            orderDetail.setQty(goodsDetail.getQuantity());
            orderDetail.setCostPrice(BigDecimal.ZERO);
            orderDetails.add(orderDetail);
        }

        payload.setOrders(CollUtil.newArrayList(order));
        offShopResource.createOtherStock(payload);

    }
}
