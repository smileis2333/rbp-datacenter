package com.regent.rbp.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.DefaultParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 员工档案查询参数
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeQueryParam extends DefaultParam {
    private List<String> code;
    private String name;
    private String notes;
    private List<Integer> sex;
    private List<Integer> status;
    private List<String> department;
    private List<String> mobile;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;
    private List<String> email;
    private List<String> idCard;
    private List<String> weixin;
    private List<String> qyweixin;
    private List<String> channelCode;
    private List<Integer> workStatus;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;
}
