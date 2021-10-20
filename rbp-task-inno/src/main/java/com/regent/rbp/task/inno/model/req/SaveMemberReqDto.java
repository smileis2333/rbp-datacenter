package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.SaveMemberDto;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-10-19 13:58
 */
@Data
public class SaveMemberReqDto extends BaseRequestDto {

    private SaveMemberDto data;
}
