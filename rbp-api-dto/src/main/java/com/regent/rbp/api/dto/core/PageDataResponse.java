package com.regent.rbp.api.dto.core;

import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class PageDataResponse<T> extends DataResponse {

    private long totalCount;

    private List<T> data;

    public PageDataResponse() {
    }

    public PageDataResponse(long totalCount, List<T> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public PageDataResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
