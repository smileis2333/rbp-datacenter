package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/02/22
 * @description
 */
@Data
public abstract class DefaultParam {
    private String fields;
    private Integer pageNo = 1;
    private Integer pageSize = 100;
}
