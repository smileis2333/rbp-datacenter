package com.regent.rbp.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description 员工档案查询返回值
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeQueryResult {
    private String code;
    private String name;
    private Integer status;
    private Integer type;
    private String sexName;
    private String department;
    private String position;
    private String mobile;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;
    private String idCard;
    private String weixin;
    private String qyweixin;
    private Integer businessManFlag;
    private Integer workStatus;
    private String channelCode;
    private String channelName;
    private String channelFullName;
    private String notes;

}
