package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/26
 * @description
 */
@Data
public class YumeiOfflineSaleOrder {
    @NotBlank
    private String outTradeNo;

    private String buyerNick;

    private String guideNo;

    private String receiver;

    private String provinceName;

    private String cityName;

    private String areaName;

    private String addrDetail;

    private String mobile;

    private String postCode;

    private String userRemark;

    @Digits(integer=15, fraction=2)
    private BigDecimal freightAmount;

    public BigDecimal getActualTotalAmount() {
        return orderItems.stream().map(e->e.getUnitPrice().multiply(e.getSkuQty())).reduce(BigDecimal.ZERO,BigDecimal::add);
    }


    public BigDecimal getGoodsQty() {
        return orderItems.stream().map(YumeiOfflineSaleOrderItem::getSkuQty).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date payTime;

    @Valid
    @NotEmpty
    private List<YumeiOfflineSaleOrderItem> orderItems;
}
