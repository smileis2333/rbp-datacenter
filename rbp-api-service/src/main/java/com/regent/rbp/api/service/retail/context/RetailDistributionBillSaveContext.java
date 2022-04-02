package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.RetailDistributionBill;
import com.regent.rbp.api.core.retail.RetailDistributionBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailDistributionBillGoods;
import com.regent.rbp.api.core.retail.RetailDistributionBillLogisticsInfo;
import com.regent.rbp.api.core.retail.RetailDistributionBillRelation;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-03-31
 * @Description
 */
@Data
public class RetailDistributionBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private RetailDistributionBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<RetailDistributionBillGoods> billGoodsList;

    @ApiModelProperty(notes = "全渠道配单顾客信息")
    private RetailDistributionBillCustomerInfo billCustomerInfo;

    @ApiModelProperty(notes = "全渠道配单关联订单信息")
    private List<RetailDistributionBillRelation> billRelationList;

    @ApiModelProperty(notes = "全渠道订单信息更新状态")
    private List<RetailOrderBillGoods> updateOrderBillGoodsList;

    @ApiModelProperty(notes = "全渠道配单物流信息")
    private List<RetailDistributionBillLogisticsInfo> billLogisticsInfoList;


}
