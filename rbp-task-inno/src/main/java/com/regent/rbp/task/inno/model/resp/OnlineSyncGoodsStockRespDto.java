package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.OnlineSyncGoodsStockPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-24
 */
@Data
public class OnlineSyncGoodsStockRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "结果")
    private List<OnlineSyncGoodsStockPageDto> data;

}
