package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chenchungui
 * @date 2022/4/3
 * @description
 */
@Data
public class YumeiOrderGoods {

    @ApiModelProperty(notes = "商品名称")
    private String goodsName;

    @ApiModelProperty(notes = "sku条形码")
    private String skuCode;

    @ApiModelProperty(notes = "sku数量")
    private BigDecimal skuQty;

    @ApiModelProperty(notes = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(notes = "订单子项状态（1：待支付、2：待发货、3：待收货、4：已取消、5：确认收货、6：已退款、7：申请售后、8：平台介入）")
    private Integer itemStatus;

    /*@ApiModelProperty(notes = "物流单号")
    private String dvyFlowNumber;*/

    @ApiModelProperty(notes = "买家商品备注")
    private String buyerRemark;

    @ApiModelProperty(notes = "卖家商品备注")
    private String sellerRemark;

    /*@ApiModelProperty(notes = "发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dvyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "取消or退款时间")
    private LocalDateTime cancelTime;*/

}
