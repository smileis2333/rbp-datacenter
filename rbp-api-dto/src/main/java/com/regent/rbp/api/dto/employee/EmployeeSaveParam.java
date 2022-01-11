package com.regent.rbp.api.dto.employee;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.List;

/**
 * @Description 员工档案保存参数
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeSaveParam {
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
    private String positionName;
    private Integer workStatus;
    private String channelCode;
    private List<CustomizeDataDto> customizeData;
}
