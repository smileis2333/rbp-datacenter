package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 更新退货单收货状态
 * @author: HaiFeng
 * @create: 2021-10-14 17:58
 */
@Data
public class UpdateRetailReceiveBackByStatusParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;

}
