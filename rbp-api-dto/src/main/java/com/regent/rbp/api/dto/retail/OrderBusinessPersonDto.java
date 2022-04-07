package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-07
 * @Description
 */
@Data
public class OrderBusinessPersonDto {

    @ApiModelProperty(notes = "导购员编号")
    private String guideNo;

    @ApiModelProperty(notes = "归属渠道编号")
    private String channelNo;
}
