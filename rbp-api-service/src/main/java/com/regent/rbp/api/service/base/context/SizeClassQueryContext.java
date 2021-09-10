package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 查询
 */
@Data
public class SizeClassQueryContext {

    @ApiModelProperty("尺码类别组")
    private List<String> sizeClassNameList;

    @ApiModelProperty("页码")
    private Integer pageNo;

    @ApiModelProperty("页数")
    private Integer pageSize;

}
