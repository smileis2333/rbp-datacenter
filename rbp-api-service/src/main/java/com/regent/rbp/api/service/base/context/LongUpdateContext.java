package com.regent.rbp.api.service.base.context;

import com.regent.rbp.api.dto.base.LongData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 内长 修改
 */
@Data
public class LongUpdateContext {

    @ApiModelProperty("内长")
    private List<LongData> longList;

}
