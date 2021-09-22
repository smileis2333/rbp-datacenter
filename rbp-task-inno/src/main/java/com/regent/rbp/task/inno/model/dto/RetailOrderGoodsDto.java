package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * 订单货品明细
 *
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderGoodsDto {

    private String order_sn;     //订单号

    private String product_sn;     //商品SKU

    private String goods_num;      //商品数量

    private String goods_real_price;      //购买商品的真实价格

    private String goods_price; //购买商品的单价

    private String suppliers_code; //SKU供应商代码

    private int sale_kind; //售卖类型，0自有商品，1跨境购，2代发商品

    private String promotion_rule_price;//	未抵扣价，促销规则之后的价钱，不算其他优惠

    private String real_bonus_value; //	实际摊分的优惠券抵扣金额

    private String real_stored_value; //	实际摊分的储值金额

    private String real_cashcoupon_value; //实际摊分的现金券抵扣金额

    private String real_integral_value; //	实际摊分的积分抵扣金额

    private String market_price; //	购买商品的吊牌价

}
