package com.regent.rbp.api.dto.employee;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.List;

/**
 * @Description 员工档案查询参数
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeQueryParam {
    private String[] code;
    private String name;
    private String[] sexName;
    private String mobile;
    private String[] channelCode;
    private String entryDate;
    private String leaveDate;
    private String jobNumber;
    private String[] positionName;
    private String[] workStatus;
    private String createdDateStart;
    private String createdDateEnd;
    private String checkDateStart;
    private String checkDateEnd;
    private String updatedDateStart;
    private String updatedDateEnd;
    private String fields;
    private Integer pageNo = 1;
    private Integer pageSize = 100;
}
