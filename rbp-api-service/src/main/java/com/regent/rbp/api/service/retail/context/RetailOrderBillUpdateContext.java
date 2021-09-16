package com.regent.rbp.api.service.retail.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全渠道订单 修改上下文对象
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
public class RetailOrderBillUpdateContext {

    @ApiModelProperty(notes = "全渠道订单号")
    private String billNo;

    @ApiModelProperty(notes = "单据状态")
    private Integer status;

    @ApiModelProperty(notes = "线上状态")
    private Integer onlineStatus;

}
