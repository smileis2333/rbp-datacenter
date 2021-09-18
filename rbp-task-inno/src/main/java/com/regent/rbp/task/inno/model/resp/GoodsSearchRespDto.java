package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.GoodsDto;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsSearchRespDto extends BaseResponseDto {
    private String totalPages;
    private List<GoodsDto> data;
}
