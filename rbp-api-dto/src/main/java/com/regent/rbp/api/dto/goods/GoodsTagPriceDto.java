package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuxing
 */
@Data
public class GoodsTagPriceDto {
    @JsonIgnore
    private Long goodsId;
    private String name;
    private BigDecimal value;
}
