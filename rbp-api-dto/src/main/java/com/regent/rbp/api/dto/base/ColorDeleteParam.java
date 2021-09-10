package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 颜色
 */
@Data
public class ColorDeleteParam {

    @ApiModelProperty("颜色编号组")
    private String[] colorCode;

}
