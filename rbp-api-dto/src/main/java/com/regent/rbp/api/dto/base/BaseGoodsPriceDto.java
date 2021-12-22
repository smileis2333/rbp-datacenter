package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenchungui
 * @date 2021-12-21
 * 折扣、结算价、吊牌价
 */
@Data
public class BaseGoodsPriceDto {

    @ApiModelProperty(notes = "货品Id")
    private Long goodsId;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    public BaseGoodsPriceDto() {

    }

    public BaseGoodsPriceDto(BigDecimal discount, BigDecimal balancePrice, BigDecimal tagPrice) {
        this.discount = discount;
        this.balancePrice = balancePrice;
        this.tagPrice = tagPrice;
    }

    public BaseGoodsPriceDto(Long goodsId, BigDecimal discount, BigDecimal balancePrice, BigDecimal tagPrice) {
        this.goodsId = goodsId;
        this.discount = discount;
        this.balancePrice = balancePrice;
        this.tagPrice = tagPrice;
    }

    public BaseGoodsPriceDto(Long goodsId, BigDecimal tagPrice) {
        this.goodsId = goodsId;
        this.tagPrice = tagPrice;
    }


}
