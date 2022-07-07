package com.regent.rbp.task.yumei.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SimpleOrderDetail {
    @NotBlank
    private String goodsName;

    @NotBlank
    private String goodsNo;

    @NotBlank
    private String skuCode;

    @NotNull
    private BigDecimal qty;
}
