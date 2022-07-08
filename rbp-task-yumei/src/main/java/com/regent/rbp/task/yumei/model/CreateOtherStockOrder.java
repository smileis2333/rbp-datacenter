package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CreateOtherStockOrder {
    @NotBlank
    private String outOrderNo;

    public BigDecimal getStockQty() {
        return orderDetails.stream().map(OrderDetail::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getStockAmount() {
        return orderDetails.stream().map(OrderDetail::getCostAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public Integer getGoodsKinds() {
        return Math.toIntExact(orderDetails.stream().map(OrderDetail::getGoodsNo).distinct().count());
    }

    private String logistics;

    private String logisticsCode;

    private String logisticsNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stockTime;

    @NotEmpty
    @Valid
    private List<OrderDetail> orderDetails;
}
