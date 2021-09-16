package com.regent.rbp.api.dto.core;

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

}
