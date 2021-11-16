package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 计划单尺码明细表
 * @author: HaiFeng
 * @create: 2021-11-15 15:20
 */
@Data
@TableName(value = "rbp_sale_plan_bill_size")
public class SalePlanBillSize {

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
