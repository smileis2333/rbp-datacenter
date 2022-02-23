package com.regent.rbp.api.service.purchaseReturn.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillSize;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知单 新增上下文对象
 * @author: HaiFeng
 * @create: 2021/12/31 10:42
 */
@Data
public class PurchaseReturnNoticeBillSaveContext {

    private PurchaseReturnNoticeBill bill;
    private List<PurchaseReturnNoticeBillGoods> billGoodsList;
    private List<PurchaseReturnNoticeBillSize> billSizeList;
    private List<CustomizeDataDto> billCustomizeDataDtos;
    private List<Map<String, Object>> goodsCustomizeData = new ArrayList<>();

    public PurchaseReturnNoticeBillSaveContext() {
        this(null);
    }

    public PurchaseReturnNoticeBillSaveContext(PurchaseReturnNoticeBillSaveParam param) {
        this.bill = new PurchaseReturnNoticeBill();
        this.bill.preInsert();
        this.bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.bill.setManualId(param.getManualId());
        this.bill.setModuleId(param.getModuleId());
        this.bill.setTaxRate(param.getTaxRate());
        this.bill.setNotes(param.getNotes());
        this.bill.setStatus(param.getStatus());
        this.bill.setProcessStatus(0);
        this.bill.setFinishFlag(0);
        this.bill.setFlowStatus(0);
        this.bill.setFlowType(1);

        this.billGoodsList = new ArrayList<>();
        this.billSizeList = new ArrayList<>();
    }

    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        if (CollUtil.isNotEmpty(customizeDataDto)) {
            HashMap<String, Object> ele = new HashMap<>();
            customizeDataDto.forEach(e -> {
                ele.put(e.getCode(), e.getValue());
            });
            ele.put("id", billGoodsId);
            goodsCustomizeData.add(ele);
        }
    }

    public void addBillGoods(PurchaseReturnNoticeBillGoods billGoods) {
        billGoods.setBillId(bill.getId());
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoodsList.add(billGoods);
    }

    public void addBillSize(PurchaseReturnNoticeBillSize billSize) {
        billSize.setBillId(bill.getId());
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSizeList.add(billSize);
    }

}
