package com.regent.rbp.api.core.omiChannel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenchungui
 * @date 2012-09-23
 */
@Data
@ApiModel(description = "线上同步货品库存信息")
@TableName(value = "rbp_online_sync_goods_stock")
public class OnlineSyncGoodsStock {

    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "电商平台")
    private Long onlinePlatformId;

    @ApiModelProperty(notes = "系统条码")
    private String barcode;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
