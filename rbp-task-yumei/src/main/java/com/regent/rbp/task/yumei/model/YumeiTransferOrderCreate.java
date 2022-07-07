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
        return orderDetails.stream().map(SimpleOrderDetail::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @NotBlank
    private String logistics;

    @NotBlank
    private String logisticsCode;

    @NotBlank
    private String logisticsNo;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date deliveryTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @NotEmpty
    @Valid
    private List<SimpleOrderDetail> orderDetails;
}
