package com.regent.rbp.api.core.stock;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
@Data
@ApiModel(description="渠道可用库存")
@TableName(value = "rbp_usable_stock_detail")
public class UsableStockDetail extends Model<UsableStockDetail> {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "货号")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "尺码")
    private Long sizeId;

    @ApiModelProperty(notes = "渠道SKU哈希值 渠道编码+货号+颜色+内长+尺码生成MD5")
    private String hashCode;

    @ApiModelProperty(notes = "SKU哈希值 货号+颜色+内长+尺码生成MD5")
    private String skuHashCode;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}
