package com.regent.rbp.task.yumei.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
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
    @Length(max = 20)
    private String goodsName;

    @NotBlank
    @Length(max = 20)
    private String skuCode;

    @NotNull
    @Digits(integer=15, fraction=2)
    private BigDecimal skuQty;

    @NotNull
    @Digits(integer=15, fraction=2)
    private BigDecimal unitPrice;

    @Length(max = 255)
    private String buyerRemark;

}
