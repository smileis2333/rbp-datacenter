package com.regent.rbp.api.dto.employee;

import lombok.Data;

import java.util.List;

/**
 * @Description 员工档案数据
 * @Author shaoqidong
 * @Date 2021/9/15
 **/
@Data
public class EmployeeDto {
    private String code;
    private String name;
    private Integer sexName;
    private String mobile;
    private String address;
    private String idCard;
    private String birthdayDate;
    private String entryDate;
    private String leaveDate;
    private String jobNumber;
    private String notes;
    private Integer positionCode;
    private Integer workStatus;
    private String channelCode;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private String updatedTime;
    private String checkBy;
    private String checkTime;
    private String cancelBy;
    private String cancelTime;
    private String uncheckBy;
}
