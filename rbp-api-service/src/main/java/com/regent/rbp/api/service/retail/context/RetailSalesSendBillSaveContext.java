package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.RetailDistributionBill;
import com.regent.rbp.api.core.retail.RetailDistributionBillGoods;
import com.regent.rbp.api.core.retail.RetailSalesSendBill;
import com.regent.rbp.api.core.retail.SalesSendBillCustomerInfo;
import com.regent.rbp.api.core.retail.SalesSendBillGoods;
import com.regent.rbp.api.core.retail.SalesSendBillLogisticsInfo;
import com.regent.rbp.api.core.retail.SalesSendBillSize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
public class RetailSalesSendBillSaveContext {

    @ApiModelProperty(notes = "单据")
    private RetailSalesSendBill bill;

    @ApiModelProperty(notes = "单据货品明细")
    private List<SalesSendBillGoods> billGoodsList;

    @ApiModelProperty(notes = "单据尺码明细")
    private List<SalesSendBillSize> billSizeList;

    @ApiModelProperty(notes = "全渠道发货单物流信息")
    private List<SalesSendBillLogisticsInfo> billLogisticsInfoList;

    @ApiModelProperty(notes = "全渠道发货单顾客信息")
    private SalesSendBillCustomerInfo billCustomerInfo;

    @ApiModelProperty(notes = "配货单单据")
    private RetailDistributionBill retailDistributionBill;

    @ApiModelProperty(notes = "配货单货品明细")
    private List<RetailDistributionBillGoods> distributionBillGoodsList;

}
