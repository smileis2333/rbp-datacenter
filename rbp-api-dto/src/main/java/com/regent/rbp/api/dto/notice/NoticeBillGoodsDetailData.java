package com.regent.rbp.api.dto.notice;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author chenchungui
 * @date 2021/12/07
 * @description
 */
@Data
public class NoticeBillGoodsDetailData {

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
        return Optional.ofNullable(this.getGoodsId()).orElse(0L).toString() + StrUtil.UNDERLINE + Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO).toString();
    }
}