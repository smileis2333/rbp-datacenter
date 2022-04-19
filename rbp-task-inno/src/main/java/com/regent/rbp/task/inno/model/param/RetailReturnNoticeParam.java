package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 退换货单请求
 * @author: HaiFeng
 * @create: 2021-09-26 13:54
 */
@Data
public class RetailReturnNoticeParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;

    @ApiModelProperty(notes = "订单号")
    private String orderSn;

    @ApiModelProperty(notes = "退货单号")
    private String returnSn;


}
