package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 内长 修改
 */
@Data
public class LongUpdateParam {

    @ApiModelProperty("内长")
    private LongData[] longData;

}
