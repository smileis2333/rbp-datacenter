package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * SKU
 */
@Data
public class SkuDto {
    /**
     * 商品SKU
     */
    private String sku;

    /**
     * 商品库存
     */
    private String product_number;

    /**
     * 商品颜色编码
     */
    private String color_code;

    /**
     * 商品颜色名称
     */
    private String color_name;

    /**
     * 商品尺码编码
     */
    private String size_code;

    /**
     * 商品尺码名称
     */
    private String size_name;

    /**
     * 商品SKU的价格
     */
    private String sku_price;

    /**
     * 商品SKU的销售价格
     */
    private String sku_sale_price;
}
