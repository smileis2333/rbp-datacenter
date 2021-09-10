package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 颜色 删除上下文
 */
@Data
public class ColorDeleteContext {

    @ApiModelProperty("颜色编号组")
    private List<String> codeList;

}
