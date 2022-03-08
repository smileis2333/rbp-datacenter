package com.regent.rbp.api.core.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description
 */
@Data
@ApiModel(description="关怀任务")
@TableName(value = "rbp_task_care")
public class TaskCare {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "任务内容(描述)")
    private String notes;

    @ApiModelProperty(notes = "任务状态 (0.未开始;1.进行中;2.已结束;3.已作废;)")
    private Integer status;

    @ApiModelProperty(notes = "任务状态名称")
    @TableField(exist = false)
    private String statusName;

    @ApiModelProperty(notes = "任务类型 (0.生日关怀;1.消费关怀;)")
    private Integer taskType;

    @ApiModelProperty(notes = "任务类型名称")
    @TableField(exist = false)
    private String taskTypeName;

    @ApiModelProperty(notes = "任务期限 (0.单次执行;1.周期执行)")
    private Integer executeType;

    @ApiModelProperty(notes = "执行频次 (0.每日;1.每周;2.每月;3.每年;99.自定义;)")
    private Integer executeFrequency;

    @ApiModelProperty(notes = "自定义的频次(多少天执行一次)")
    private Integer customFrequencyDays;

    @ApiModelProperty(notes = "执行开始日期")
    private Date beginTime;

    @ApiModelProperty(notes = "执行结束日期")
    private Date endTime;

    @ApiModelProperty(notes = "限制回访天数")
    private Integer returnVisitDays;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}
