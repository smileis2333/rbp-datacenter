package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
public class RetailSalesSendBillLogisticsInfoDto {

    @ApiModelProperty(notes = "物流公司编号")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "物流费用")
    private BigDecimal logisticsAmount;

    @ApiModelProperty(notes = "货运单号。若未传全渠道配单单据编号，则必传")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "物流说明")
    private String notes;
}
