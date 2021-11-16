package com.regent.rbp.api.dto.calculate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 采购单欠数明细
 * @author: HaiFeng
 * @create: 2021-11-16 16:17
 */
@Data
public class PurchaseBillOweDetail {

    @ApiModelProperty(notes = "指令单Id")
    private Long purchaseId;

    @ApiModelProperty(notes = "货品Id")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色Id")
    private Long colorId;

    @ApiModelProperty(notes = "内长Id")
    private Long longId;

    @ApiModelProperty(notes = "尺码Id")
    private Long sizeId;

    @ApiModelProperty(notes = "欠数数量")
    private BigDecimal quantity;

}
