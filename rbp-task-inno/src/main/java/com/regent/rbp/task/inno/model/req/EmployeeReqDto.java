package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.EmployeeDto;
import lombok.Data;

import java.util.List;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
@Data
public class EmployeeReqDto extends BaseRequestDto {
    private List<EmployeeDto> data;
}
