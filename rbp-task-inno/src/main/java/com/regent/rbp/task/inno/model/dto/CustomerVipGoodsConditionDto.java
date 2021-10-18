package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerVipGoodsConditionDto {
    private BigDecimal AmountLimit;
    private BigDecimal DiscountLimit;
    private String FieldName;
    private String Value;
}
