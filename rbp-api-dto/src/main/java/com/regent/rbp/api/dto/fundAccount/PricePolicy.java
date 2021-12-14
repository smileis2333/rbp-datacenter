package com.regent.rbp.api.dto.fundAccount;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 价格政策
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class PricePolicy {
    private String priceTypeName;
    private BigDecimal discount;
}
