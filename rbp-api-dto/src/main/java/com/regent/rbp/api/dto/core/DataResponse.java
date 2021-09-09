package com.regent.rbp.api.dto.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class DataResponse {
    @ApiModelProperty(
            notes = "响应编码"
    )
    protected Integer code;

    @ApiModelProperty(
            notes = "响应消息"
    )
    protected String message;
}
