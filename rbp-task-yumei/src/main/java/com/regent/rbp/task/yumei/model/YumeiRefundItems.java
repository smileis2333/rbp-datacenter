package com.regent.rbp.task.yumei.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-19
 * @Description
 */
@Data
public class YumeiRefundItems {

    @ApiModelProperty(notes = "sku条形码")
    private String skuCode;

    @ApiModelProperty(notes = "物流公司名称")
    private String logisticsName;

    @ApiModelProperty(notes = "物流单号")
    private String logisticsNo;
}
