package com.regent.rbp.task.inno.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-24 15:07
 */
@Data
public class MemberRespDto extends BaseResponseDto {
    private List<StatusResponseDto> data;
}
