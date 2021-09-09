package com.regent.rbp.api.dto.goods;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsPriceDto {
    private BigDecimal machiningPrice;
    private BigDecimal materialPrice;
    private BigDecimal planCostPrice;
    private BigDecimal purchasePrice;

    private List<GoodsTagPriceDto> tagPrice;
}
