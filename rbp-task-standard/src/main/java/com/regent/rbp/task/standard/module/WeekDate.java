package com.regent.rbp.task.standard.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 日期周
 * @author: HaiFeng
 * @create: 2021-11-17 17:02
 */
@Data
public class WeekDate {

    public WeekDate(Long startTime, Long endTime) {
        this.startTime = new Date(startTime);
        this.endTime = new Date(endTime);
    }

    public WeekDate(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @ApiModelProperty(notes = "开始日期")
    private Date startTime;

    @ApiModelProperty(notes = "截止日期")
    private Date endTime;
}
