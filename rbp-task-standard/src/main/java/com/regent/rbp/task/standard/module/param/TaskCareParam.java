package com.regent.rbp.task.standard.module.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description 关怀任务参数
 */
@Data
public class TaskCareParam {

    @ApiModelProperty(notes = "关怀任务编号列表")
    private List<String> taskCodeList;
}
