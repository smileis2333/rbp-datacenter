package com.regent.rbp.api.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsPriceDto {
    @ApiModelProperty("加工价")
    private BigDecimal machiningPrice;
    @ApiModelProperty("物料价")
    private BigDecimal materialPrice;
    @ApiModelProperty("计划成本价")
    private BigDecimal planCostPrice;
    @ApiModelProperty("进货价")
    private BigDecimal purchasePrice;

    @Valid
    @ApiModelProperty("吊牌价格列表")
    private List<GoodsTagPriceDto> tagPrice;
}
