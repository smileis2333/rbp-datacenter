package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class YumeiReturnOrder {
    @NotBlank
    private String outOrderNo;

    @NotBlank
    private String basicOffshopCode;

    @NotBlank
    private String basicOffshopName;

    @NotBlank
    private String basicWarehouseCode;

    @NotBlank
    private String basicWarehouseName;

    @NotBlank
    private String applyUser;

    @NotBlank
    private String reviewUser;


    public BigDecimal getApplyQty() {
        return orderDetails.stream().map(OrderDetail::getQty).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal getTotalCost() {
        return orderDetails.stream().map(OrderDetail::getCostAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @NotNull
    private BigDecimal stockInQty = BigDecimal.ZERO;

    @NotNull
    private BigDecimal stockInAmount = BigDecimal.ZERO;

    @NotBlank
    private String logistics;

    @NotBlank
    private String logisticsCode;

    @NotBlank
    private String logisticsNo;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Valid
    @NotEmpty
    private List<OrderDetail> orderDetails;

}
