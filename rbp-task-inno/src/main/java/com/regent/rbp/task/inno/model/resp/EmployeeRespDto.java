package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.infrastructure.response.BaseResponse;

import java.util.List;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
public class EmployeeRespDto extends BaseResponseDto {
    private List<StatusResponseDto> data;
}
