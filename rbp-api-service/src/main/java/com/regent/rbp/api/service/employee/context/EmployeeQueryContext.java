package com.regent.rbp.api.service.employee.context;

import lombok.Data;

import java.util.Date;

/**
 * @Description 员工查询档案上下文
 * @Author shaoqidong
 * @Date 2021/9/15
 **/
@Data
public class EmployeeQueryContext {
    private String[]  code;
    private String  name;
    private long[]  sex;
    private String[]  channelCode;
    private long[]  channelId;
    private String  mobile;
    private String  entryDate;
    private String leaveDate;
    private String jobNumber;
    private String[] positionName;
    private long[] positionId;
    private String[] workStatus;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;

}
