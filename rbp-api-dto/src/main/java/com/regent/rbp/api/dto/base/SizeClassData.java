package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 查询
 */
@Data
public class SizeClassData {

    @ApiModelProperty("尺码类别")
    private String sizeClassName;

    @ApiModelProperty("尺码列表")
    private String[] size;

}
