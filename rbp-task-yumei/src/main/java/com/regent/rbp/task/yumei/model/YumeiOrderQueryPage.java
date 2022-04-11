package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author chenchungui
 * @date 2022/4/3
 * @description
 */
@Data
public class YumeiOrderQueryPage {

    @ApiModelProperty(notes = "订单编号")
    private String outOrderNo;

    @ApiModelProperty(notes = "门店编号")
    private String storeNo;

    /*@ApiModelProperty(notes = "订单来源(1：美人计会员商城、2：酒会员商城、3：丽晶）")
    private Integer orderSource;*/

    @ApiModelProperty(notes = "订单状态（1：待支付、2：待发货、3：待收货、4：已取消、5：确认收货、6：已退款、7：申请售后、8：平台介入）")
    private Integer status;

    @ApiModelProperty(notes = "支付标识（0：未支付、1：已支付）")
    private Integer payFlag;

    @ApiModelProperty(notes = "支付方式（1：微信、2：支付宝、3：银联）")
    private Integer payWay;

    @ApiModelProperty(notes = "订单总件数")
    private BigDecimal goodsQty;

    /*@ApiModelProperty(notes = "支付订单号")
    private String payOrderNo;*/

    /*@ApiModelProperty(notes = "银行流水号")
    private String tradeNumber;*/

    @ApiModelProperty(notes = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "取消or退款时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty(notes = "订单子项")
    private List<YumeiOrderGoods> orderItems;

}
