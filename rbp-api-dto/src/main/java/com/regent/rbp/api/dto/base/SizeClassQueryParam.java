package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 查询
 */
@Data
public class SizeClassQueryParam {

    @ApiModelProperty("尺码类别组")
    private String[] sizeClassName;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
