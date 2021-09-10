package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色
 */
@Data
public class ColorData {

    @ApiModelProperty("颜色编号")
    private String colorCode;

    @ApiModelProperty("颜色名称")
    private String colorName;

    @ApiModelProperty("颜色组")
    private String colorGroup;

}
