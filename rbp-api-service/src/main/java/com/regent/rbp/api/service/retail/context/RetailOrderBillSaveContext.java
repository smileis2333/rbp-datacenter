package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 全渠道订单 保存上下文对象
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
public class RetailOrderBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private RetailOrderBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<RetailOrderBillGoods> billGoodsList;

    @ApiModelProperty(notes = "全渠道订单付款信息")
    private List<RetailOrderBillPaymentInfo> billPaymentInfoList;

    @ApiModelProperty(notes = "全渠道订单顾客信息")
    private RetailOrderBillCustomerInfo billCustomerInfo;

    @ApiModelProperty(notes = "分销信息")
    private List<RetailOrderBillDstb> billDstbList;


}
