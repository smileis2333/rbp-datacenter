package com.regent.rbp.api.core.purchaseReceiveNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author huangjie
 * @date : 2021/12/22
 * 采购到货通知单货品明细
 * @description
 */
@Data
@TableName(value = "rbp_purchase_receive_notice_bill_goods")
public class PurchaseReceiveNoticeBillGoods  {
    private Long id;
    private Long billId;
    private Long goodsId;
    private BigDecimal discount;
    private BigDecimal tagPrice;
    private BigDecimal balancePrice;
    private BigDecimal currencyPrice;
    private BigDecimal exchangeRate;
    private BigDecimal quantity;
    private String remark;
}
