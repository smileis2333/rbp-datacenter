package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
public class RetailOrderSampleDto {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "线上订单号")
    private String onlineOrderCode;

    @ApiModelProperty(notes = "平台类型 关联online_platform_type.id")
    private Integer onlinePlatformTypeId;

    @ApiModelProperty(notes = "电商平台 关联rbp_online_platform.id")
    private Long onlinePlatformId;

    @ApiModelProperty(notes = "电商平台编号")
    private String onlinePlatformCode;

}
