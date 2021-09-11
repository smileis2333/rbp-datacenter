package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义字段
 * @author xuxing
 */
@Data
@AllArgsConstructor
public class CustomizeDataDto {
    private String code;
    private String value;
}
