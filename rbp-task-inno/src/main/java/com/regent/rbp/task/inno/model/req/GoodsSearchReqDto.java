package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.GoodsSearchDto;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsSearchReqDto extends BaseRequestDto {
    private GoodsSearchDto data;
}
