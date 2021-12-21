package com.regent.rbp.api.service.purchase.context;

import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoodsFinal;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSize;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSizeFinal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 采购订单 保存上下文对象
 *
 * @author chenchungui
 * @date 2021-12-21
 */
@Data
public class PurchaseBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private PurchaseBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<PurchaseBillGoods> billGoodsList;

    @ApiModelProperty(notes = "单据货品调整明细")
    private List<PurchaseBillGoodsFinal> billGoodsFinalList;

    @ApiModelProperty(notes = "单据尺码明细")
    private List<PurchaseBillSize> billSizeList;

    @ApiModelProperty(notes = "单据调整后尺码明细")
    private List<PurchaseBillSizeFinal> billSizeFinalList;

}
