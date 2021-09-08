package com.regent.rbp.api.core.goods;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "货品吊牌价")
@TableName(value = "rbp_goods_tag_price")
public class GoodsTagPrice {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "吊牌价格类型")
    private Long priceTypeId;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal price;
}
