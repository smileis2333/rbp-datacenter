package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * 订单
 *
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderItemDto {

    private String order_sn; //	订单号

    private String outer_order_sn; //	ERP订单号

    private String pickup_code; //	取货码(物流方式为店铺自提,shipping_code= ‘instore’)

    private String User_id; //	用户ID

    private String user_name; //	用户编码（下单用户手机号码）

    private String card_num; //	用户卡号

    private String order_status; //	订单状态 0：未确认，1：已确认，2：已取消，3：无效，4：退货，7：拒收。

    private String pay_status; //	付款状态  0：未付款，2：已付款

    private String shipping_code; //	快递公司code

    private String shipping_name; //	快递公司名字

    private String province; //	省份名字

    private String city; //	城市名字

    private String distinct; //	区域名字

    private String zip; //	邮编

    private String address; //	地址

    private String consigee; //	联系人

    private String mobile; //	联系人电话号码

    private String tel; //	联系人电话号码

    private String pay_code; //	付款方式code

    private String pay_name; //	付款方式名称（例如：微信支付）

    private String add_time; //	生成订单时间

    private String pay_time; //	付款时间

    private String store_code; //	店铺code

    private String staff_code; //	店员code

    private String goods_amount; //	订单商品金额

    private String order_amount; //	订单需要支付金额

    private String invoice_no; //	快递单号

    private String shipping_fee; //	运费

    private String integral_money; //	积分抵扣金额

    private String discount_manual; //	促销优惠金额

    private String bouns_money; //	优惠劵抵扣金额

    private String surplus; //	余额

    private String platform_src; //	订单来源（MWIN大屏，WAP网页，MAPP手机端）

    private String alipay_sn; //	支付交易单号

    private String pack_fee; //	红包金额

    private String coupon_money; //	现金券金额

    private String lastModifiedTime; //	最后修改时间

    private String real_name; //	用户昵称

    private String user_id; //	用户ID

    private String in_store_code; //取货店铺

    private String offline_surplus; //线下余额

    private String bonus_sn; //	该订单使用的优惠券号

    private String coupon_codes; //	该订单使用的现金券号，多个用逗号隔开

    private String share_staff_code; //	分享下单店员编码

    private String is_forecd_shop_self; //	是否强制店铺自提，1表示强制，0表示不是

    private String admin_remark; //	客服留言

    private String user_remark; //	买家留言

    private String remarkLastModifyTime; //	备注最后修改时间

    private int orderType; //订单类型，0普通订单，1拼团订单，2预售订单

    private String warehouse_code; //	仓库代码

    private String Store_name; //	下单店铺名称

    private String Instore_name; //	自提店铺名称

    private String original_shipping_fee; //	原始运费

    private String deduction_shipping_fee; //	抵扣运费

    private int exchange_integral; //兑换积分

    private String estimate_delivery_time; //	预计发货时间，1900-01-01表示没有设置

}
