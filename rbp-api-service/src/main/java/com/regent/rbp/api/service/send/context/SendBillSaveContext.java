package com.regent.rbp.api.service.send.context;


import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.salePlan.SalePlanBillSizeFinal;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.core.sendBill.SendBillLogistics;
import com.regent.rbp.api.core.sendBill.SendBillSize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发货订单 保存上下文对象
 *
 * @author chenchungui
 * @date 2021-12-16
 */
@Data
public class SendBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private SendBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<SendBillGoods> billGoodsList;

    @ApiModelProperty(notes = "单据尺码明细")
    private List<SendBillSize> billSizeList;

    @ApiModelProperty(notes = "物流信息")
    private SendBillLogistics logistics;

    @ApiModelProperty(notes = "更新指令单据尺码明细欠数")
    private List<NoticeBillSize> updateNoticeBillSizeList;

    @ApiModelProperty(notes = "更新计划据尺码明细欠数")
    private List<SalePlanBillSizeFinal> updateSalePlanBillSizeFinals;

}
