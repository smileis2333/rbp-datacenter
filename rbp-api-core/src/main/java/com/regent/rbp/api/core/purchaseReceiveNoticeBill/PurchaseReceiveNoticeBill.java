package com.regent.rbp.api.core.purchaseReceiveNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2021/12/22
 * 采购到货通知单
 * @description
 */
@Data
@TableName(value = "rbp_purchase_receive_notice_bill")
public class PurchaseReceiveNoticeBill extends BillMasterData {
    private Long purchaseId;
    private BigDecimal taxRate ;
    private Long currencyTypeId;
    private Long businessTypeId;
    private Long supplierId;
    private Long toChannelId;

}
