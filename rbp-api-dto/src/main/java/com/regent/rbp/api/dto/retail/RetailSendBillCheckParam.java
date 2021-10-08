package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillCheckParam {

    @ApiModelProperty(notes = "全渠道发货单号")
    private String billNo;

}
