package com.regent.rbp.task.yumei.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/05/26
 * @description
 */
@Data
public class YumeiOfflineSaleOrderItem {
    @NotBlank
    private String goodsName;

    @NotBlank
    private String skuCode;

    @NotNull
    private BigDecimal skuQty;

    @NotNull
    private BigDecimal unitPrice;

    private String buyerRemark;

    private String outRefundNo;

    private String refundRemark;

}
