package com.regent.rbp.api.core.purchaseBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 采购单尺码明细 对象 rbp_notice_bill_size
 *
 * @author chenchungui
 * @date 2021-12-21
 */
@Data
@ApiModel(description = "采购单调整后尺码明细 ")
@TableName(value = "rbp_purchase_bill_size_final")
public class PurchaseBillSizeFinal extends BillGoodsSizeData {


}
