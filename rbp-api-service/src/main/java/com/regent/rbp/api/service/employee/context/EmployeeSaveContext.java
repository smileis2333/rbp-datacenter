package com.regent.rbp.api.service.employee.context;

import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

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

    public EmployeeSaveContext(EmployeeSaveParam param) {
        this.employee = new Employee();
        Long userId = ThreadLocalGroup.getUserId();
        this.employee.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.employee.setCreatedBy(userId);
        this.employee.setUpdatedBy(userId);

        if (param != null) {
            this.readProperties(param);
        }
    }

    private void readProperties(EmployeeSaveParam param) {
        this.employee.setCode(param.getCode());
        this.employee.setName(param.getName());
        this.employee.setMobile(param.getMobile());
        this.employee.setIdCard(param.getIdCard());
        this.employee.setNotes(param.getNotes());
        this.employee.setWorkStatus(param.getWorkStatus());
        this.employee.setBirthdayDate(param.getBirthdayDate());
    }

}
