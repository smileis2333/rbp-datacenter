package com.regent.rbp.api.core.purchaseReturnNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(description = "采购退货通知单尺码明细")
@TableName(value = "rbp_purchase_return_bill_size")
public class PurchaseReturnBillSize extends BillGoodsSizeData {
}
