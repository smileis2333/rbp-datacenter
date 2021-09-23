package com.regent.rbp.api.service.employee.context;

import com.netflix.discovery.converters.Auto;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description 员工档案上下文
 * @Author shaoqidong
 * @Date 2021/9/15
 **/
@Data
public class EmployeeSaveContext {
    private Employee employee;

    public EmployeeSaveContext() {
    }

    public EmployeeSaveContext( EmployeeSaveParam param) {
        this.employee = new  Employee();
        Long userId = ThreadLocalGroup.getUserId();
        this.employee.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.employee.setCreatedBy(userId);
        this.employee.setUpdatedBy(userId);

        if(param != null) {
            this.readProperties(param);
        }
    }

    private void readProperties(EmployeeSaveParam param) {
        this.employee.setCode(param.getChannelCode());
        this.employee.setName(param.getName());
        this.employee.setMobile(param.getMobile());
        this.employee.setAddress(param.getAddress());
        this.employee.setIdCard(param.getIdCard());
        this.employee.setJobNumber(param.getJobNumber());
        this.employee.setNotes(param.getNotes());
        this.employee.setWorkStatus(param.getWorkStatus());
        Date birthdayDate = DateUtil.getDate(param.getBirthdayDate(), DateUtil.FULL_DATE_FORMAT);
        this.employee.setBirthdayDate(birthdayDate);
        Date entryDate = DateUtil.getDate(param.getEntryDate(), DateUtil.FULL_DATE_FORMAT);
        this.employee.setEntryDate(entryDate);
        Date leaveDate = DateUtil.getDate(param.getLeaveDate(), DateUtil.FULL_DATE_FORMAT);
        this.employee.setBirthdayDate(leaveDate);
    }


}
