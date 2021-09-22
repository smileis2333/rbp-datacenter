package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.RetailOrderSearchPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderSearchRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "结果")
    private RetailOrderSearchPageDto data;

}
