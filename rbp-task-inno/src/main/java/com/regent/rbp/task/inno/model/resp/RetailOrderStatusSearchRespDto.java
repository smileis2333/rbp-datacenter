package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.RetailOrderSearchPageDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderStatusSearchPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-15
 * @Description
 */
@Data
public class RetailOrderStatusSearchRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "结果")
    private RetailOrderStatusSearchPageDto data;
}
