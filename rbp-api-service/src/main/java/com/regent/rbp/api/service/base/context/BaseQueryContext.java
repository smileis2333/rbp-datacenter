package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 基础资料 查询上下文
 */
@Data
public class BaseQueryContext {

    @ApiModelProperty("资料类别")
    private String type;

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
