package com.regent.rbp.api.core.employee;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/15
 **/
@Data
@ApiModel(description = "员工档案")
@TableName(value = "rbp_employee")
public class Employee {
    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "性别;关联性别档案rbp_sex")
    private Long sexId;

    @ApiModelProperty(value = "员工编号")
    private String code;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "家庭地址")
    private String address;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "生日")
    private Date birthdayDate;

    @ApiModelProperty(value = "入职日期")
    private Date entryDate;

    @ApiModelProperty(value = "离职日期")
    private Date leaveDate;

    @ApiModelProperty(value = "工作卡号")
    private String jobNumber;

    @ApiModelProperty(value = "备注")
    private String notes;

    @ApiModelProperty(value = "职位;关联职位档案rbp_position")
    private Long positionId;

    @ApiModelProperty(value = "工作状态 1-入职 2-离职 3-实习")
    private Integer workStatus;

    @ApiModelProperty(value = "所属渠道编码")
    private Long channelId;

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

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;
}
