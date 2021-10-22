package com.regent.rbp.api.dto.core;

import com.regent.rbp.api.dto.constants.ApiResponseCode;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class ListDataResponse<T> extends DataResponse {

    private List<T> data;

    public ListDataResponse() {
        super();
    }

    public ListDataResponse(List<T> data) {
        super();
        this.data = data;
    }

    public ListDataResponse(Integer code, String message) {
        super(code, message);
    }

    public static ListDataResponse Success(List data) {
        return new ListDataResponse(data);
    }

    public static ListDataResponse errorParameter(String message) {
        return new ListDataResponse(ApiResponseCode.API_PARAMETER_ERROR, message);
    }
}
