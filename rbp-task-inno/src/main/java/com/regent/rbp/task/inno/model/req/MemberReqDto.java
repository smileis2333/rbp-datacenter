package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.InnoMemberDto;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 会员同步请求
 * @author: HaiFeng
 * @create: 2021-09-24 13:32
 */
@Data
public class MemberReqDto extends BaseRequestDto {
    private List<InnoMemberDto> data;
}
