package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeDto;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 14:06
 */
@Data
public class RetailReturnNoticeReqDto extends BaseRequestDto {

    private RetailReturnNoticeDto data;

}
