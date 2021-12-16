package com.regent.rbp.api.core.sendBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 指令单尺码明细 对象 rbp_notice_bill_size
 *
 * @author chenchungui
 * @date 2021-12-07
 */
@Data
@ApiModel(description = "发货单尺码明细 ")
@TableName(value = "rbp_send_bill_size")
public class SendBillSize extends BillGoodsSizeData {

}
