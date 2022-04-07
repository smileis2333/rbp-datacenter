package com.regent.rbp.api.dto.retail;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2022-04-07
 * @Description
 */
@Data
public class RetalOrderGoodsInfoDto {

    private String goodsName;
    private String skuCode;
    private BigDecimal skuQty;
    private BigDecimal unitPrice;
    private String buyerRemark;
}
