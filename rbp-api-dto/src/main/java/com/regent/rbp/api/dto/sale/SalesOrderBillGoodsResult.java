package com.regent.rbp.api.dto.sale;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 销售单货品明细
 * @author: HaiFeng
 * @create: 2021-11-08 14:29
 */
@Data
public class SalesOrderBillGoodsResult {

    @ApiModelProperty(notes = "条形码。注：条形码和「货号、颜色编号、内长、尺码」二选一。")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "状态")
    private Integer status;

    @ApiModelProperty(notes = "销售类型(0.正常销售、1.预售直发、2.预售自提、3.全渠道发货)")
    private Integer saleType;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "零售价")
    private BigDecimal retailPrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal salesPrice;

    @ApiModelProperty(notes = "原始实卖价")
    private BigDecimal originalPrice;

    @ApiModelProperty(notes = "数量。非计量货品的数量必须是1，可以分多行")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "积分")
    private BigDecimal point;

    @ApiModelProperty(notes = "吊牌折扣")
    private BigDecimal tagDiscount;

    @ApiModelProperty(notes = "零售折扣")
    private BigDecimal retailDiscount;

    @ApiModelProperty(notes = "结算折扣")
    private BigDecimal balanceDiscount;
}
