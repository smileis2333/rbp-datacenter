package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
public class RetailSalesSendBillGoodsDetailData {

    @ApiModelProperty(notes = "条形码")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "销售类型(3.全渠道发货)")
    private Integer saleType;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "数量。非计量货品的数量必须是1，可以分多行")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String notes;
}
