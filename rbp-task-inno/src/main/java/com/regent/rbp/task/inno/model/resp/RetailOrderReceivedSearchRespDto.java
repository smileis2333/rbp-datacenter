package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.RetailOrderReceivedSearchPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-19
 * @Description
 */
@Data
public class RetailOrderReceivedSearchRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "结果")
    private RetailOrderReceivedSearchPageDto data;
}
