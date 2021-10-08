package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.RetailReturnNoticePageDto;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 16:13
 */
@Data
public class RetailReturnNoticeRespDto extends BaseResponseDto {

    private RetailReturnNoticePageDto data;
}
