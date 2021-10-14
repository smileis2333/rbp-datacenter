package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 16:19
 */
@Data
public class RetailReturnNoticeListDto {

    /**
     * App退换货SN
     */
    private String return_sn;

    /**
     * ERP订单SN
     */
    private String erp_order_sn;

    /**
     * App订单SN
     */
    private String order_sn;

    /**
     * 总额
     */
    private BigDecimal totalamount;

    /**
     * 银行名称
     */
    private String bank_name;

    /**
     * 户名
     */
    private String bank_user;

    /**
     * 银行账号
     */
    private String bank_account;

    /**
     * 退货原因
     */
    private String return_reason;

    /**
     * 类型，1退货
     */
    private Integer return_type;

    /**
     * 最后修改时间
     */
    private Date lastModifiedTime;

    /**
     * 下单店铺
     */
    private String store_code;

    /**
     * 下单会员
     */
    private String card_num;

    /**
     * 退回总积分
     */
    private Integer return_point;

    /**
     * 退回总储值
     */
    private BigDecimal return_stored_value;

    /**
     * 退款类型，0退款退货，1仅退款不退货
     */
    private Integer refund_type;

    /**
     * 物流单号
     */
    private String shipping_no;

    /**
     * 商品明细
     */
    private List<RetailReturnNoticeListDetailDto> listDetail;

}
