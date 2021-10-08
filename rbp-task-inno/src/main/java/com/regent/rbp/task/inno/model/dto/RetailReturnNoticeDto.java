package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 14:10
 */
@Data
public class RetailReturnNoticeDto {

    @ApiModelProperty(notes = "开始时间（时间标准化：2021-01-01 00:00:01）")
    private String beginTime;

    @ApiModelProperty(notes = "结束时间（时间标准化：2021-01-02 00:00:01）")
    private String endTime;

    @ApiModelProperty(notes = "订单号（可选）")
    private String orderSn;

    @ApiModelProperty(notes = "退货单号（可选）")
    private String returnSn;

    @ApiModelProperty(notes = "获取的页码（第一页开始）")
    private Integer pageIndex;
}
