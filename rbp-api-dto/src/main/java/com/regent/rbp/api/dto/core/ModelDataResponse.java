package com.regent.rbp.api.dto.core;

import com.regent.rbp.api.dto.constants.ApiResponseCode;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class ModelDataResponse<T> extends DataResponse {

    private T data;

    public ModelDataResponse() {
        super();
    }

    public ModelDataResponse(T data) {
        super();
        this.data = data;
    }

    public ModelDataResponse(Integer code, String message) {
        super(code, message);
    }

    public static ModelDataResponse Success(Object data) {
        return new ModelDataResponse(data);
    }

    public static ModelDataResponse errorParameter(String message) {
        return new ModelDataResponse(ApiResponseCode.API_PARAMETER_ERROR, message);
    }

}
