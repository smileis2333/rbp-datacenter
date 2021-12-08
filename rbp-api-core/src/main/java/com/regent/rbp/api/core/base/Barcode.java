package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.MD5Util;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "条形码")
@TableName(value = "rbp_barcode")
public class Barcode {
    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色编码")
    private Long colorId;

    @ApiModelProperty(notes = "内长编码")
    private Long longId;

    @ApiModelProperty(notes = "尺码编码")
    private Long sizeId;

    @ApiModelProperty(notes = "条码")
    private String barcode;

    @ApiModelProperty(notes = "条码规则类型")
    private Long ruleId;

    public String getSingleCode() {
        return MD5Util.shortenKeyString(this.getGoodsId(), this.getColorId(), this.getLongId(), this.getSizeId());
    }
}
