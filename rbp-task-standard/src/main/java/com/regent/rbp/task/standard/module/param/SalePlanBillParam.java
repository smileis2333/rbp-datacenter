package com.regent.rbp.task.standard.module.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 销售计划自动完结请求
 * @author: HaiFeng
 * @create: 2021-11-15 18:31
 */
@Data
public class SalePlanBillParam {

    @ApiModelProperty(notes = "订单号")
    private String billNo;
}
