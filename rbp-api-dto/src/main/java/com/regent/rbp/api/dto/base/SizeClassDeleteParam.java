package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 删除
 */
@Data
public class SizeClassDeleteParam {

    @ApiModelProperty("尺码类别")
    private String sizeClassName;

}
