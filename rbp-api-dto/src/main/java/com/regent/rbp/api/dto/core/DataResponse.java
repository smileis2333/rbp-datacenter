package com.regent.rbp.api.dto.core;

import com.regent.rbp.infrastructure.constants.ResponseCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class DataResponse {

    @ApiModelProperty(notes = "响应编码")
    protected Integer code = ResponseCode.OK;

    @ApiModelProperty(notes = "响应消息")
    protected String message = "ok";

    public DataResponse() {
    }

    public DataResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static DataResponse Success() {
        return new DataResponse();
    }

}
