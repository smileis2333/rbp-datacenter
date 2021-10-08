package com.regent.rbp.api.dto.retail;

import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单货品明细
 * @author: HaiFeng
 * @create: 2021-09-27 15:43
 */
@Data
public class RetailReturnNoticeBillGoodsDetailData {

    @ApiModelProperty(notes = "条形码。注：条形码和「货号、颜色编号、内长、尺码」二选一。")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "数量。非计量货品的数量必须是1，可以分多行")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;



}
