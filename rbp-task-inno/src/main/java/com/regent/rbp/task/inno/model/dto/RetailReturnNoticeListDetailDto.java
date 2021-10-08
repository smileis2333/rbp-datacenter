package com.regent.rbp.task.inno.model.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 16:22
 */
@Data
public class RetailReturnNoticeListDetailDto {

    /**
     * 商品sku
     */
    private String sku;
    /**
     * 商品数量
     */
    private Integer goods_number;

    /**
     * 商品原价
     */
    private BigDecimal goods_price;

    /**
     * 商品单价
     */
    private BigDecimal real_price;

    /**
     * 商品原始SKU
     */
    private String source_sku;

    /**
     * SKU供应商代码
     */
    private String suppliers_code;

    /**
     * 售卖类型，0自有商品，1跨境购，2代发商品
     */
    private Integer sale_kind;

    /**
     * 退回积分（需乘以数量）
     */
    private Integer Real_point;

    /**
     * 退回储值（需乘以数量）
     */
    private BigDecimal Real_stored_value;
}
