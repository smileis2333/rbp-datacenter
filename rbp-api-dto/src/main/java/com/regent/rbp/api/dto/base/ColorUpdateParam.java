package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 修改
 */
@Data
public class ColorUpdateParam {

    @ApiModelProperty("颜色组")
    private List<ColorData> colorData;

}
