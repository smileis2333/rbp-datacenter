package com.regent.rbp.task.inno.model.dto;

import com.regent.rbp.task.inno.model.req.BaseRequestDto;
import com.regent.rbp.task.inno.model.resp.BaseResponseDto;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-10-19 10:19
 */
@Data
public class UpdateReturnOrderStatusReqDto extends BaseRequestDto {

    private List<UpdateReturnOrderStatusDto> data;
}
