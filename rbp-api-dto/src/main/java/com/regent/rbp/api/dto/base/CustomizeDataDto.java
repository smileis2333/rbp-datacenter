package com.regent.rbp.api.dto.base;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 自定义表的id与其主表保持一致，由系统控制生成，不允许用户进行改id的设置
     * ref https://stackoverflow.com/a/38846455/10827862
     */
    @Pattern(regexp = "^(?:(?!id).)*$", message = "自定义字段不允许为'id'")
    @ApiModelProperty("字段编码" +
            "code和name必填其中一个，都传值时以code为准")
    private String code;

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段值")
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
