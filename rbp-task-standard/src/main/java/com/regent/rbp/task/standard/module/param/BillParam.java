package com.regent.rbp.task.standard.module.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售计划自动完结请求
 * @author: HaiFeng
 * @create: 2021-11-15 18:31
 */
@Data
public class BillParam {

    @ApiModelProperty(notes = "订单号")
    private String billNo;
}
