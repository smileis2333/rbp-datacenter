package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:25
 */
@Data
public class CouponPolicyDownLoadParam {
    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;
}
