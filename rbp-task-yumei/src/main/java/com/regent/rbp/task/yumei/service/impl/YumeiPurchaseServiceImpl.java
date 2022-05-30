package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillGoodsDetailData;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.task.yumei.config.yumei.api.PurchaseResource;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveBillOrder;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveBillOrderItem;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveOrderPayload;
import com.regent.rbp.task.yumei.service.YumeiPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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


    @Override
    @Transactional
    public void createPurchaseReceive(String billNo) {
        ReceiveBillQueryParam param = new ReceiveBillQueryParam();
        param.setBillNo(billNo);
        PageDataResponse<ReceiveBillQueryResult> query = receiveBillService.query(param);
        if (CollUtil.isNotEmpty(query.getData())) {

            ReceiveBillQueryResult bill = query.getData().get(0);
            YumeiPurchaseReceiveOrderPayload payload = new YumeiPurchaseReceiveOrderPayload();
            payload.setStoreNo(billNo);
            payload.setRequestId(UUID.fastUUID().toString());
            List<Long> goodsId = CollUtil.distinct(CollUtil.map(bill.getGoodsDetailData(), ReceiveBillGoodsDetailData::getGoodsId, true));
            Map<Long, Map<Long, Barcode>> barcodeMap = barcodeDao.selectList(Wrappers.lambdaQuery(Barcode.class).in(Barcode::getGoodsId, goodsId)).stream().collect(Collectors.groupingBy(Barcode::getGoodsId, Collectors.collectingAndThen(Collectors.toMap(Barcode::getId, Function.identity()), Collections::unmodifiableMap)));
            ArrayList<YumeiPurchaseReceiveBillOrder> orders = new ArrayList<>();
            YumeiPurchaseReceiveBillOrder order = new YumeiPurchaseReceiveBillOrder();
            order.setOutOrderNo(billNo);

            order.setDeliveryOrderNo(bill.getSendNo());
            order.setBasicOffshopCode(bill.getChannelCode());
            order.setBasicOffshopName(bill.getChannelName());
            order.setPoInTime(bill.getCreatedTime());
            List<YumeiPurchaseReceiveBillOrderItem> orderItems = new ArrayList<>();

            bill.getGoodsDetailData().forEach(gd -> {
                YumeiPurchaseReceiveBillOrderItem orderItem = new YumeiPurchaseReceiveBillOrderItem();
                orderItem.setGoodsName(gd.getGoodsName());
                orderItem.setGoodsNo(gd.getGoodsCode());
                Map<Long, Barcode> barcodes = barcodeMap.get(gd.getGoodsId());
                orderItem.setSkuCode(barcodes.values().stream().findFirst().get().getBarcode());
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
}
