package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.MemberPageDto;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 会员信息
 * @author: HaiFeng
 * @create: 2021-10-19 14:16
 */
@Data
public class MemberPageRespDto {

    private Integer totalPages;

    private List<MemberPageDto> data;
}
