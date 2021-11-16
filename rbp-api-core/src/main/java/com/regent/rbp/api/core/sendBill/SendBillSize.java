package com.regent.rbp.api.core.sendBill;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 发货单尺码明细表
 * @author: HaiFeng
 * @create: 2021-11-16 17:35
 */
@Data
public class SendBillSize {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
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

    @ApiModelProperty(notes = "尺码")
    private Long sizeId;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;
}
