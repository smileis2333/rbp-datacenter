package com.regent.rbp.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.List;

/**
 * @Description 员工档案查询返回值
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeQueryResult {
    @JsonIgnore
    private String code;
    private String name;
    private String sexName;
    private String mobile;
    private String address;
    private String idCard;
    private String birthdayDate;
    private String entryDate;
    private String leaveDate;
    private String jobNumber;
    private String notes;
    private String positionCode;
    private String positionName;
    private Integer workStatus;
    private String channelCode;
    private String createdTime;
    private String updatedTime;
    private String checkTime;
//    private Integer code;
//    private String message;
//    private Integer totalCount;
//    private List<EmployeeDto> employeeData;
    private List<CustomizeDataDto> customizeData;
}
