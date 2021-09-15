package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 全渠道订单
 * @Author chenchungui
 * @Date 2021-09-14
 **/
@Data
public class RetailOrderBillUpdateParam {

    @ApiModelProperty(notes = "全渠道订单号")
    private String billNo;

    @ApiModelProperty(notes = "单据状态")
    private Integer status;

    @ApiModelProperty(notes = "线上状态")
    private Integer onlineStatus;

}
