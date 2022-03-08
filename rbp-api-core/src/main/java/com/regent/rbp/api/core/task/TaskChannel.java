package com.regent.rbp.api.core.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description
 */
@Data
@ApiModel(description="任务渠道范围关系")
@TableName(value = "rbp_task_channel")
public class TaskChannel {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "任务主信息编码")
    private Long taskId;

    @ApiModelProperty(notes = "组号")
    private String groupNo;

    @ApiModelProperty(notes = "条件KEY")
    private String conditionKey;

    @ApiModelProperty(notes = "条件值")
    private String conditionValue;
}
