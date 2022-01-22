package com.regent.rbp.api.service.purchase.context;

import com.regent.rbp.api.core.box.BoxBill;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillSize;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class PurchaseReceiveNoticeBillSaveContext {

    private PurchaseReceiveNoticeBill bill;
    private List<PurchaseReceiveNoticeBillGoods> billGoodsList = new ArrayList<>();
    private List<PurchaseReceiveNoticeBillSize> billSizeList = new ArrayList<>();
    private List<CustomizeDataDto>billCustomizeDataDtos;
    private List<Map<String, Object>> goodsCustomizeData = new ArrayList<>();
    private List<BoxBill>boxBills = new ArrayList<>();

    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        HashMap<String, Object> ele = new HashMap<>();
        customizeDataDto.forEach(e -> {
            ele.put(e.getCode(), e.getValue());
        });
        ele.put("id", billGoodsId);
        goodsCustomizeData.add(ele);
    }

    public void addBillGoods(PurchaseReceiveNoticeBillGoods billGoods){
        billGoods.setBillId(bill.getId());
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoodsList.add(billGoods);
    }

    public void addBillSize(PurchaseReceiveNoticeBillSize billSize){
        billSize.setBillId(bill.getId());
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSizeList.add(billSize);
    }
}
