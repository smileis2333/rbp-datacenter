package com.regent.rbp.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.Dictionary;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 员工档案保存参数
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeSaveParam {
    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String notes;

    @DiscreteRange(ranges = {100, 101}, message = "状态(100-启用 101-禁用)")
    @NotNull
    private Integer status;

    @DiscreteRange(ranges = {0}, message = "类型(0-普通用户)")
    @NotNull
    private Integer type;

    @NotBlank
    private String password;

    @DiscreteRange(ranges = {0, 1}, message = "性别(0.男1.女)")
    private Integer sex;

    private String department;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    private String email;

    private String idCard;

    private String weixin;

    private String qyweixin;

    @DiscreteRange(ranges = {1}, message = "营业员标记(1-是)")
    @NotNull
    private Integer businessManFlag;

    @DiscreteRange(ranges = {0, 1, 2}, message = "工作状态(0-在职;1-离职; 2-实习)")
    @NotNull
    private Integer workStatus;

    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    @ApiModelProperty(notes = "职位")
    @Dictionary(targetTable = "rbp_position", targetField = "name")
    private String position;

}
