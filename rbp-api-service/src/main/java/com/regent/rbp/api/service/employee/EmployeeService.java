package com.regent.rbp.api.service.employee;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.employee.EmployeeQueryParam;
import com.regent.rbp.api.dto.employee.EmployeeQueryResult;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
public interface EmployeeService {
    /**
     * 查询
     * @param param
     * @return
     */
    PageDataResponse<EmployeeQueryResult> query(EmployeeQueryParam param);

    /**
     * 新增/修改
     * @param param
     * @return
     */
    DataResponse save(EmployeeSaveParam param);

}
