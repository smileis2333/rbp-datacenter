package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderSearchDto {

    @ApiModelProperty(notes = "开始时间（时间标准化：2021-01-01 00:00:01）")
    private String beginTime;

    @ApiModelProperty(notes = "结束时间（时间标准化：2021-01-02 00:00:01）")
    private String endTime;

    @ApiModelProperty(notes = "订单SN(订单编号 例如： “201510010003,201510010004”）")
    private String order_sn_list;

    @ApiModelProperty(notes = "获取的页码（第一页开始）")
    private Integer pageIndex;

}
