package com.regent.rbp.api.service.base.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 内长 删除
 */
@Data
public class LongDeleteContext {

    @ApiModelProperty("内长")
    private List<String> longNameList;

}
