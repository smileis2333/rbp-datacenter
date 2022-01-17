package com.regent.rbp.api.dto.goods;

import lombok.Data;

import javax.validation.Valid;
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

    @Valid
    private List<GoodsTagPriceDto> tagPrice;
}
