package com.regent.rbp.api.dto.fundAccount;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 品牌价格政策
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class BrandPricePolicy {
    private String brandName;
    private String discountCategoryName;
    private String priceTypeName;
    private BigDecimal discount;
}
