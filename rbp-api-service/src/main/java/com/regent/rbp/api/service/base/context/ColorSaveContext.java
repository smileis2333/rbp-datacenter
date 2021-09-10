package com.regent.rbp.api.service.base.context;

import com.regent.rbp.api.dto.base.ColorData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 保存上下文
 */
@Data
public class ColorSaveContext {

    @ApiModelProperty("颜色组")
    private List<ColorData> list;

}
