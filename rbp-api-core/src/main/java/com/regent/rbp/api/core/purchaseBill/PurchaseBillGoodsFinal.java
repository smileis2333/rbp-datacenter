package com.regent.rbp.api.core.purchaseBill;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 采购单调整后货品明细
 * @author: HaiFeng
 * @create: 2021-11-17 11:04
 */
@Data
@ApiModel(description = "采购单调整后货品明细")
@TableName(value = "rbp_purchase_bill_goods_final")
public class PurchaseBillGoodsFinal {

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

    @ApiModelProperty(notes = "采购价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "交货日期")
    private Date deliveryDate;

    @ApiModelProperty(notes = "来货超差比例")
    private BigDecimal receiveDifferentPercent;

    @ApiModelProperty(notes = "来货超差类型")
    private Integer receiveDifferentType;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;

}
