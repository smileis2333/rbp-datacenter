package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-23
 */
@Data
public class OnlineSyncGoodsStockParam {

    @ApiModelProperty(notes = "平台编码")
    private String onlinePlatformCode;

}
