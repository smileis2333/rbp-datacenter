package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 自定义字段
 *
 * @author xuxing
 */
@Data
public class CustomizeDataDto {

    @JsonIgnore
    private Long id;

    private String code;

    private String value;

    public CustomizeDataDto() {
    }

    public CustomizeDataDto(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
