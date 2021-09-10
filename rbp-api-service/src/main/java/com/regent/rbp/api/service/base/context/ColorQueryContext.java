package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 查询上下文
 */
@Data
public class ColorQueryContext {

    @ApiModelProperty("颜色编号")
    private List<String> codeList;

    @ApiModelProperty("颜色组")
    private String groupName;

    @ApiModelProperty("需返回的字段列表")
    private String fields;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
