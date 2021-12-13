package com.regent.rbp.api.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description
 */
@Data
public class UserCashierChannelDto {

    @ApiModelProperty(notes = "渠道编号")
    private String channelCode;

}
