package com.regent.rbp.task.yumei.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 订单子项信息
 * @author: HaiFeng
 * @create: 2022/4/3 13:47
 */
@Data
public class YumeiOrderItems {

    @ApiModelProperty(notes = "sku条形码")
    private String skuCode;

    @ApiModelProperty(notes = "sku退货数量")
    private BigDecimal skuQty;

    @ApiModelProperty(notes = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(notes = "退款备注")
    private String refundRemark;

}
