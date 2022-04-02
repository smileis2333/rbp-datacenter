package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-01-13
 * @Description
 */
@Data
@ApiModel(description="发货销售单尺码明细表 ")
@TableName(value = "rbp_sales_send_bill_size")
public class SalesSendBillSize {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "货品明细编码")
    private Long billGoodsId;

    @ApiModelProperty(notes = "货号")
    private Long goodsId;

    @ApiModelProperty(notes = "货品编号")
    @TableField(exist = false)
    private String goodsCode;

    @ApiModelProperty(notes = "货品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "颜色编号")
    @TableField(exist = false)
    private String colorNo;

    @ApiModelProperty(notes = "颜色名称")
    @TableField(exist = false)
    private String colorName;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "内长名称")
    @TableField(exist = false)
    private String longName;

    @ApiModelProperty(notes = "尺码id, 就是size_detail_id")
    private Long sizeId;

    @ApiModelProperty(notes = "尺码名称")
    @TableField(exist = false)
    private String sizeName;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "序号")
    private Integer rowIndex;

    @ApiModelProperty(notes = "唯一码编码")
    private Long labelId;

    @ApiModelProperty(notes = "唯一码编号")
    @TableField(exist = false)
    private String labelCode;

    @ApiModelProperty(notes = "条码编码")
    private Long barcodeId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建人名称")
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setCreatedBy(ThreadLocalGroup.getUserId());
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setCreatedTime(date);
        setUpdatedTime(date);
    }

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }
}
