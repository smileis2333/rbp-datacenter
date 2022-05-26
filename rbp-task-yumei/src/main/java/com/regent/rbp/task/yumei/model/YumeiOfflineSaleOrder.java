package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
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
    @Length(max = 40)
    @NotBlank
    private String outTradeNo;

    @Length(max = 40)
    @NotBlank
    private String buyerNick;

    @Length(max = 50)
    private String guideNo;

    @NotBlank
    @Length(max = 40)
    private String receiver;

    @NotBlank
    @Length(max = 20)
    private String provinceName;

    @NotBlank
    @Length(max = 20)
    private String cityName;

    @NotBlank
    @Length(max = 20)
    private String areaName;

    @NotBlank
    @Length(max = 255)
    private String addrDetail;

    @NotBlank
    @Length(max = 255)
    private String mobile;

    @Length(max = 40)
    private String postCode;

    @Length(max = 255)
    private String userRemark;

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
