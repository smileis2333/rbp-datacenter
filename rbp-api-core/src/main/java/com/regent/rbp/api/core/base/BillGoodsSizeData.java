package com.regent.rbp.api.core.base;

import com.regent.rbp.infrastructure.util.MD5Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 尺码明细基类
 *
 * @author chenchungui
 * @date 2021/12/07
 */
@Data
public class BillGoodsSizeData {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "货品明细编码")
    private Long billGoodsId;

    @ApiModelProperty(notes = "货号")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "尺码id, 就是size_detail_id")
    private Long sizeId;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    public String getSingleCode() {
        return MD5Util.shortenKeyString(this.getGoodsId(), this.getColorId(), this.getLongId(), this.getSizeId());
    }

}
