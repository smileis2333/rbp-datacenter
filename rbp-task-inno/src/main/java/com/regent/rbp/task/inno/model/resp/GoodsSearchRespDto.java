package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.GoodsSearchPageDto;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class GoodsSearchRespDto extends BaseResponseDto {
    private GoodsSearchPageDto data;


}
