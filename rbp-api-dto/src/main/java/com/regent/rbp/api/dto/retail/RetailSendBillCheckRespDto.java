package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillCheckRespDto {

    @ApiModelProperty(notes = "全渠道订单单号")
    private String billNo;

    @ApiModelProperty(notes = "是否能发货，1能发货，0不能发货")
    private Integer canDelivery;

    @ApiModelProperty(notes = "不能发货原因")
    private String reason;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailSendBillGoodsCheckRespDto> billGoodsList;

}
