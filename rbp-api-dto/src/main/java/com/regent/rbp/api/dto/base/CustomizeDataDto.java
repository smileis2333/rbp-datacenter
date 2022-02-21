package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 自定义字段
 *
 * @author xuxing
 */
@Data
public class CustomizeDataDto {

    @JsonIgnore
    private Long id;

    /**
     * 自定义表的id与其主表保持一致，由系统控制生成，不允许用户进行改id的设置
     * ref https://stackoverflow.com/a/38846455/10827862
     */
    @Pattern(regexp = "^(?:(?!id).)*$",message = "自定义字段不允许为'id'")
    private String code;

    private String value;

    public CustomizeDataDto() {
    }

    public CustomizeDataDto(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
