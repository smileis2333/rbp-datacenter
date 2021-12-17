package com.regent.rbp.api.dto.send;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author chenchungui
 * @date 2021/12/16
 * @description
 */
@Data
public class SendBillGoodsDetailData {

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @ApiModelProperty(notes = "指令单号")
    private String noticeNo;

    @ApiModelProperty(notes = "条形码")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "行ID")
    @JsonIgnore
    private Long columnId;

    @ApiModelProperty(notes = "指令单ID")
    @JsonIgnore
    private Long noticeId;

    @ApiModelProperty(notes = "销售计划ID")
    @JsonIgnore
    private Long salePlanId;

    @ApiModelProperty(notes = "货品ID")
    @JsonIgnore
    private Long goodsId;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

    /**
     * 同款多价，根据货品ID+价格分组
     *
     * @return
     */
    public String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s_%s", this.getNoticeId(), this.getGoodsId(), balancePriceStr);
    }
}
