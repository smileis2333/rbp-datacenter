package com.regent.rbp.api.core.purchaseReceiveBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(description = "采购入库单尺码明细 ")
@TableName(value = "rbp_purchase_receive_bill_size")
public class PurchaseReceiveBillSize extends BillGoodsSizeData {

}
