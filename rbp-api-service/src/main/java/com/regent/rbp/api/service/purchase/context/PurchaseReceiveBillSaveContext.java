package com.regent.rbp.api.service.purchase.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBill;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBillGoods;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBillSize;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class PurchaseReceiveBillSaveContext {

    private PurchaseReceiveBill bill;
    private List<PurchaseReceiveBillGoods> billGoodsList = new ArrayList<>();
    private List<PurchaseReceiveBillSize> billSizeList = new ArrayList<>();
    private List<CustomizeDataDto>billCustomizeDataDtos = new ArrayList<>();
    private List<Map<String, Object>> goodsCustomizeData = new ArrayList<>();
    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        if (CollUtil.isEmpty(customizeDataDto)){
            return;
        }
        HashMap<String, Object> ele = new HashMap<>();
        customizeDataDto.forEach(e -> {
            ele.put(e.getCode(), e.getValue());
        });
        ele.put("id", billGoodsId);
        goodsCustomizeData.add(ele);
    }

    public void addBillGoods(PurchaseReceiveBillGoods billGoods){
        billGoods.setBillId(bill.getId());
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoodsList.add(billGoods);
    }

    public void addBillSize(PurchaseReceiveBillSize billSize){
        billSize.setBillId(bill.getId());
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSizeList.add(billSize);
    }
}
