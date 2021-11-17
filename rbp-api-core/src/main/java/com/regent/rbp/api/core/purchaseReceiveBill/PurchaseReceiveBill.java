package com.regent.rbp.api.core.purchaseReceiveBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 采购入库单
 * @author: HaiFeng
 * @create: 2021-11-17 10:47
 */
@Data
@ApiModel(description = "采购入库单")
@TableName(value = "rbp_purchase_receive_bill")
public class PurchaseReceiveBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "供应商编码")
    private Long supplierId;

    @ApiModelProperty(notes = "收货渠道")
    private Long toChannelId;

    @ApiModelProperty(notes = "采购单号")
    private Long purchaseId;

    @ApiModelProperty(notes = "采购到货通知单号")
    private Long noticeId;

    @ApiModelProperty(notes = "收货工作台单编码")
    private Long receiveBenchId;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;
}
