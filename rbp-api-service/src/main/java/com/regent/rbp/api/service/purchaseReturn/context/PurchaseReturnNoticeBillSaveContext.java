package com.regent.rbp.api.service.purchaseReturn.context;

import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillSize;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知单 新增上下文对象
 * @author: HaiFeng
 * @create: 2021/12/31 10:42
 */
@Data
public class PurchaseReturnNoticeBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private PurchaseReturnNoticeBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<PurchaseReturnNoticeBillGoods> billGoodsList;

    @ApiModelProperty(notes = "单据尺码明细")
    private List<PurchaseReturnNoticeBillSize> billSizeList;

    public PurchaseReturnNoticeBillSaveContext() { this(null); }

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

}
