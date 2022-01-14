package com.regent.rbp.api.web.employee;

import com.regent.rbp.api.dto.employee.EmployeeQueryParam;
import com.regent.rbp.api.dto.employee.EmployeeQueryResult;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.api.service.employee.EmployeeService;
import com.regent.rbp.api.web.constants.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@RestController
@RequestMapping(ApiConstants.API_EMPLOYEE)
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/query")
    public PageDataResponse<EmployeeQueryResult> query(@RequestBody EmployeeQueryParam param) {
        PageDataResponse<EmployeeQueryResult> result = employeeService.query(param);
        return result;
    }

    @PostMapping
    public DataResponse save(@RequestBody @Valid EmployeeSaveParam param) {
        DataResponse result = employeeService.save(param);
        return result;
    }
}
