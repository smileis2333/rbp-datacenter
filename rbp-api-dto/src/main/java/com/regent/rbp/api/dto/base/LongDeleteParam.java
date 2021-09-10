package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 内长 删除
 */
@Data
public class LongDeleteParam {

    @ApiModelProperty("内长")
    private String[] longName;

}
