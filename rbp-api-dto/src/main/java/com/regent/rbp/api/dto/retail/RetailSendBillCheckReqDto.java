package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillCheckReqDto {

    @ApiModelProperty(notes = "全渠道订单单号")
    private String billNo;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailSendBillGoodsCheckReqDto> billGoodsList;

}
