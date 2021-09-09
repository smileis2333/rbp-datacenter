package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 基础资料
 */
@Data
public class BaseData {

    @ApiModelProperty("编号")
    private String code;

    @ApiModelProperty("名称")
    private String name;

}
