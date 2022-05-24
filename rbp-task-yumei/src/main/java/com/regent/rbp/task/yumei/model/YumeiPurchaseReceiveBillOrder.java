package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
 * @date : 2022/05/23
 * @description
 */
@Data
public class YumeiPurchaseReceiveBillOrder {
    @Length(max = 40)
    @NotBlank
    private String outOrderNo;

    @Length(max = 40)
    @NotBlank
    private String deliveryOrderNo;

    @Length(max = 16)
    @NotBlank
    private String bizOrderType;

    @Length(max = 16)
    @NotBlank
    private String busType;

    @Length(max = 32)
    @NotBlank
    private String basicOffshopCode;

    @Length(max = 100)
    @NotBlank
    private String basicOffshopName;

    @NotNull
    private Integer goodsKinds;

    @Length(max = 40)
    private String batchNo;

    @NotNull
    @Digits(integer = 20, fraction = 0)
    private BigDecimal planInQty;

    @NotNull
    @Digits(integer = 20, fraction = 0)
    private String actualInQty;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date poInTime;

    @NotNull
    @Digits(integer = 20, fraction = 0)
    private BigDecimal totalPurchaseAmount;

    @NotNull
    @Digits(integer = 20, fraction = 0)
    private BigDecimal totalTaxIncludedPurchaseAmount;

    @NotEmpty
    @Valid
    private List<YumeiPurchaseReceiveBillOrderItem> orderDetails;
}
