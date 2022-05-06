package com.regent.rbp.task.yumei.dto;

import com.regent.rbp.task.yumei.model.YumeiFinancialSettlementBill;
import com.regent.rbp.task.yumei.model.YumeiFinancialSettlementBillGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@Data
public class FinancialSettlementBillContext {

    @ApiModelProperty(notes = "单据")
    private YumeiFinancialSettlementBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<YumeiFinancialSettlementBillGoods> billGoodsList;
}
