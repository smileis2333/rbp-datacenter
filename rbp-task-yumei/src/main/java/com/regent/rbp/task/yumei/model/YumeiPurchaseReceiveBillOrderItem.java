package com.regent.rbp.task.yumei.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/05/23
 * @description
 */
@Data
public class YumeiPurchaseReceiveBillOrderItem {
    @NotBlank
    private String goodsName;

    @NotBlank
    private String goodsNo;

    @NotBlank
    private String skuCode;

    @NotNull
//    @Digits(integer=15, fraction=2)
    private BigDecimal poInQty;

    @NotNull
//    @Digits(integer=15, fraction=2)
    private BigDecimal actualPoInQty;

    @NotNull
//    @Digits(integer=15, fraction=2)
    private BigDecimal taxIncludedPurchaseUnitPrice;

    @NotNull
//    @Digits(integer=15, fraction=2)
    private BigDecimal excludingTaxPurchaseUnitPrice;

    public BigDecimal getTaxPurchaseAmount() {
        return actualPoInQty.multiply(taxIncludedPurchaseUnitPrice);
    }

    public BigDecimal getPurchaseAmount() {
        return actualPoInQty.multiply(excludingTaxPurchaseUnitPrice);
    }
}
