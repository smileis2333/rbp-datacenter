package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 基础资料 查询
 */
@Data
public class BaseQueryParam {

    @ApiModelProperty("资料类别")
    private String type;

    @ApiModelProperty("值")
    private String data;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
