package com.regent.rbp.api.core.purchaseReceiveNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@ApiModel(description="采购到货通知单尺码明细 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_purchase_receive_notice_bill_size")
public class PurchaseReceiveNoticeBillSize extends BillGoodsSizeData {
}
