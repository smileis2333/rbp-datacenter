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
}
