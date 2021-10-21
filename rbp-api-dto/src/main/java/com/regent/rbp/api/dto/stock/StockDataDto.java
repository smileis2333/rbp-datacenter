package com.regent.rbp.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 库存明细
 * @author: HaiFeng
 * @create: 2021-10-21 11:08
 */
@Data
public class StockDataDto {

    @JsonIgnore
    private Long channelId;
    @JsonIgnore
    private Long goodsId;
    @JsonIgnore
    private Long colorId;
    @JsonIgnore
    private Long longId;
    @JsonIgnore
    private Long sizeId;

    @ApiModelProperty(notes = "库存单位编号")
    private String unitNo;

    @ApiModelProperty(notes = "货号")
    private String goodsNo;

    @ApiModelProperty(notes = "颜色编号")
    private String colorNo;

    @ApiModelProperty(notes = "内长")
    private String longNo;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "数量")
    private Integer quantity;

}
