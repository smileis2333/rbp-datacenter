package com.regent.rbp.task.yumei.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
@ApiModel(description = "收款单尺码明细")
@TableName(value = "yumei_receipt_bill_size")
public class YumeiReceiptBillSize extends BillGoodsSizeData {
}
