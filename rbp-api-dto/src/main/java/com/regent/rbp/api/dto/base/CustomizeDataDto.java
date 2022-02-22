package com.regent.rbp.api.dto.base;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
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
    @Pattern(regexp = "^(?:(?!id).)*$", message = "自定义字段不允许为'id'")
    private String code;

    private String name;

    private String value;

    public CustomizeDataDto() {
    }

    public CustomizeDataDto(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @AssertTrue(message = "code和name二选一")
    private boolean isCode() {
        return code != null || name != null;
    }

    public String getCode() {
        if (StrUtil.isNotEmpty(code)) {
            return code;
        } else if (StrUtil.isNotEmpty(name))
            return name;
        return null;
    }
}
