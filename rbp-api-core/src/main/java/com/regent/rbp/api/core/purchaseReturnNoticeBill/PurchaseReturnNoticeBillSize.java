package com.regent.rbp.api.core.purchaseReturnNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知单尺码明细
 * @author: HaiFeng
 * @create: 2021/12/30 17:43
 */
@Data
@ApiModel(description = "采购退货通知单尺码明细")
@TableName(value = "rbp_purchase_return_notice_bill_size")
public class PurchaseReturnNoticeBillSize extends BillGoodsSizeData {
}
