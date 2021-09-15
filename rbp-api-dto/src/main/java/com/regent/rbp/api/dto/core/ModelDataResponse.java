package com.regent.rbp.api.dto.core;

/**
 * @author xuxing
 */
public class ModelDataResponse<T> extends DataResponse {

    private T data;

    public ModelDataResponse() {
        super();
    }

    public ModelDataResponse(T data) {
        super();
        this.data = data;
    }

    public static ModelDataResponse Success(Object data) {
        return new ModelDataResponse(data);
    }

}
