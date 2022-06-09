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
public class YumeiTransferOrderCreate {
    @NotBlank
    private String transferOrderNo;

    @NotBlank
    private String outOrderNo;

    @NotBlank
    private String basicInOffshopCode;

    @NotBlank
    private String basicInOffshopName;

    @NotBlank
    private String basicOutOffshopCode;

    @NotBlank
    private String basicOutOffshopName;

    @NotBlank
    private String applyUser;

    public BigDecimal getApplyQty() {
        return orderDetails.stream().map(OrderDetail::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCost() {
        return orderDetails.stream().map(OrderDetail::getCostAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @NotNull
    private BigDecimal stockInQty = BigDecimal.ZERO;

    private BigDecimal stockInAmount = BigDecimal.ZERO;

    private BigDecimal stockOutQty = BigDecimal.ZERO;

    private BigDecimal stockOutAmount = BigDecimal.ZERO;

    @NotBlank
    private String logistics;

    @NotBlank
    private String logisticsCode;

    @NotBlank
    private String logisticsNo;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @NotEmpty
    @Valid
    private List<OrderDetail> orderDetails;
}
