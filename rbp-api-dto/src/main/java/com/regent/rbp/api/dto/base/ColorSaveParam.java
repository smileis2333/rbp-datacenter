package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 保存
 */
@Data
public class ColorSaveParam {

    @ApiModelProperty("颜色组")
    private List<ColorData> colorData;

}
