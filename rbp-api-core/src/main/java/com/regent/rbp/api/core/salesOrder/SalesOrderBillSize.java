package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单尺码明细
 * @author: HaiFeng
 * @create: 2021-11-08 17:47
 */
@Data
@ApiModel(description="销售单尺码明细表")
@TableName(value = "rbp_sales_order_bill_size")
public class SalesOrderBillSize {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "货品明细编码")
    private Long billGoodsId;

    @ApiModelProperty(notes = "序号")
    private Integer rowIndex;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色编码")
    private Long colorId;

    @ApiModelProperty(notes = "内长编码")
    private Long longId;

    @ApiModelProperty(notes = "尺码编码")
    private Long sizeId;

    @ApiModelProperty(notes = "唯一码编码")
    private Long labelId;

    @ApiModelProperty(notes = "条码编码")
    private Long barcodeId;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @TableField(exist = false)
    private String goodsName;
}
