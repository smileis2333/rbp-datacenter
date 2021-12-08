package com.regent.rbp.api.service.notice.context;

import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.noticeBill.NoticeBillLogistics;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.salePlan.SalePlanBillSizeFinal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 指令订单 保存上下文对象
 *
 * @author chenchungui
 * @date 2021-12-07
 */
@Data
public class NoticeBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private NoticeBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<NoticeBillGoods> billGoodsList;

    @ApiModelProperty(notes = "单据尺码明细")
    private List<NoticeBillSize> billSizeList;

    @ApiModelProperty(notes = "物流信息")
    private NoticeBillLogistics logistics;

    @ApiModelProperty(notes = "更新计划单据尺码明细欠数")
    private List<SalePlanBillSizeFinal> updateSalePlanBillSizeFinalList;

}
