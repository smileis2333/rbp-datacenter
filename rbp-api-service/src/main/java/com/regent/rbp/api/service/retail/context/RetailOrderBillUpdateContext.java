package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.core.retail.RetailOrderBillPaymentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 全渠道订单 修改上下文对象
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
public class RetailOrderBillUpdateContext {

    @ApiModelProperty(notes = "单据")
    private RetailOrderBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<RetailOrderBillGoods> billGoodsList;

    @ApiModelProperty(notes = "全渠道订单付款信息")
    private List<RetailOrderBillPaymentInfo> billPaymentInfoList;

    @ApiModelProperty(notes = "全渠道订单顾客信息")
    private RetailOrderBillCustomerInfo billCustomerInfo;

}
