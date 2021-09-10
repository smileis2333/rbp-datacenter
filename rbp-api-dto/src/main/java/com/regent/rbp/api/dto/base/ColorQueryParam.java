package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 查询
 */
@Data
public class ColorQueryParam {

    @ApiModelProperty("颜色编号")
    private String[] colorCode;

    @ApiModelProperty("颜色组")
    private String colorGroup;

    @ApiModelProperty("需返回的字段列表")
    private String fields;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
