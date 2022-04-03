package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.core.retail.RetailReceiveBackBillGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-03
 * @Description
 */
@Data
public class RetailReceiveBackBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private RetailReceiveBackBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<RetailReceiveBackBillGoods> billGoodsList;
}
