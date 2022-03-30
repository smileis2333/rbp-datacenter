package com.regent.rbp.api.dto.sourcing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.infrastructure.util.MD5Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新品保护dto
 * @author Moruins
 * @date 2022-03-28
 */
@Data
public class TaskChannelNewGoodsGuardDto {

    @ApiModelProperty(notes = "渠道id")
    private Long channelId;

    @ApiModelProperty(notes = "货品id")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色id")
    private Long colorId;

    @ApiModelProperty(notes = "内长id")
    private Long longId;

    @ApiModelProperty(notes = "尺码id")
    private Long sizeId;

    @JsonIgnore
    public String getSkuKey() {
        return MD5Util.shortenKeyString(this.getGoodsId(), this.getColorId(), this.getLongId(),this.getSizeId());
    }
}
