package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品
 * @author xuxing
 */
@Data
public class GoodsItemDto {
    /**
     * 商品ID
     */
    private int goods_id;
    /**
     * 商品货号
     */
    private String goods_sn;

    /**
     * 分类ID
     */
    private int cat_id;
    /**
     * 商品名称
     */
    private String goods_name;

    /**
     * 类别(普通商品，换购商品，促销商品，赠品)
     */
    private String Type;
    /**
     * 商品价格
     */
    private BigDecimal Price;
    /**
     * 销售价格
     */
    private BigDecimal SalePrice;

    /**
     * 积分类型(默认空)
     */
    private String Integral_type;
    /**
     * 积分
     */
    private int Integral;
    /**
     * 商品详情链接
     */
    private String SmallImg;
    /**
     * 保留字段(默认空)
     */
    private String Param2;
    /**
     * 销量(默认0)
     */
    private int UnitSales;
    /**
     * 商品图片
     */
    private String goods_img;
    /**
     * 商品的最低吊牌价
     */
    private BigDecimal MinPrice;
    /**
     * 商品的最高吊牌价
     */
    private BigDecimal MaxPrice;
    /**
     * 商品的最低售价
     */
    private BigDecimal MinSalePrice;
    /**
     * 商品的最高售价
     */
    private BigDecimal MaxSalePrice;
    /**
     * 商品详情描述(html代码)
     */
    private String goods_desc;
    /**
     * 上架时间
     */
    private String onSaleTime;
    /**
     * 商品库存
     */
    private int goodsNumber;
    /**
     * 商品上架状态，0下架，1上架
     */
    private int isOnSale;

    /**
     * 商品sku相关属性
     */
    private List<SkuDto> list_sku_property;
}
