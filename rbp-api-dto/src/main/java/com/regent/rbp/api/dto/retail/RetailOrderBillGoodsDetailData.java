package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenchungui
 * @date 2021/9/14
 * @description
 */
@Data
public class RetailOrderBillGoodsDetailData {

    @ApiModelProperty(notes = "条形码")
    private String barcode;

    @ApiModelProperty(notes = "销售类型 0-销售、1-现货直发、2-现货自提、3-预售直发、4-预售自提")
    private Integer saleType;

    @ApiModelProperty(notes = "货品类型 0-普通、1-赠品、2-换货")
    private Integer retailGoodsType;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;


}
