package com.regent.rbp.api.core.purchaseReceiveBill;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 采购入库单货品明细
 * @author: HaiFeng
 * @create: 2021-11-17 10:50
 */
@Data
@ApiModel(description = "采购入库单货品明细")
@TableName(value = "rbp_purchase_receive_bill_goods")
public class PurchaseReceiveBillGoods {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;
}
