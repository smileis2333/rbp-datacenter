package com.regent.rbp.api.core.purchaseReturnNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel(description = "采购退货单")
@TableName(value = "rbp_purchase_return_bill")
public class PurchaseReturnBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "供应商编码")
    private Long supplierId;

    @ApiModelProperty(notes = "退货渠道编号")
    private Long channelId;

    @ApiModelProperty(notes = "采购单号")
    private Long purchaseId;

    private Long noticeNo;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;
}
