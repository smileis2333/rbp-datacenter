package com.regent.rbp.api.service.purchaseReturn.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBillSize;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class PurchaseReturnBillSaveContext {
    private PurchaseReturnBill bill;
    private List<PurchaseReturnBillGoods> billGoodsList = new ArrayList<>();
    private List<PurchaseReturnBillSize> billSizeList = new ArrayList<>();
    private List<Map<String, Object>> goodsCustomizeData = new ArrayList<>();
    private List<CustomizeDataDto>billCustomizeDataDtos;

    public void addBillGoods(PurchaseReturnBillGoods billGoods){
        billGoods.setBillId(bill.getId());
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoodsList.add(billGoods);
    }

    public void addBillSize(PurchaseReturnBillSize billSize){
        billSize.setBillId(bill.getId());
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSizeList.add(billSize);
    }


    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        HashMap<String, Object> ele = new HashMap<>();
        if (CollUtil.isEmpty(customizeDataDto)){
            return;
        }
        customizeDataDto.forEach(e -> {
            ele.put(e.getCode(), e.getValue());
        });
        ele.put("id", billGoodsId);
        goodsCustomizeData.add(ele);
    }
}
