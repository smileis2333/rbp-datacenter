package com.regent.rbp.api.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuxing
 */
@Data
public class GoodsTagPriceData {
    private String id;
    private BigDecimal value;
}
