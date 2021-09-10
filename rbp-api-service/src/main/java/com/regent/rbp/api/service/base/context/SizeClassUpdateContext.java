package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 修改
 */
@Data
public class SizeClassUpdateContext {

    @ApiModelProperty("尺码类别")
    private String sizeClassName;

    @ApiModelProperty("尺码列表")
    private List<String> sizeList;

}
