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

    private final String bizOrderType = "PURCHASE_ORDER";

    private final String busType = "STORE_STOCKIN";

    @Length(max = 32)
    @NotBlank
    private String basicOffshopCode;

    @Length(max = 100)
    @NotBlank
    private String basicOffshopName;


    public Integer getGoodsKinds() {
        return Math.toIntExact(orderDetails.stream().map(YumeiPurchaseReceiveBillOrderItem::getGoodsNo).distinct().count());
    }

    @Length(max = 40)
    private String batchNo;

    public BigDecimal getPlanInQty() {
        return orderDetails.stream().map(YumeiPurchaseReceiveBillOrderItem::getPoInQty).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal getActualInQty() {
        return orderDetails.stream().map(YumeiPurchaseReceiveBillOrderItem::getActualPoInQty).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal getTotalPurchaseAmount() {
        return orderDetails.stream().map(YumeiPurchaseReceiveBillOrderItem::getPurchaseAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public BigDecimal getTotalTaxIncludedPurchaseAmount() {
        return orderDetails.stream().map(YumeiPurchaseReceiveBillOrderItem::getTaxPurchaseAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date poInTime;

    @NotEmpty
    @Valid
    private List<YumeiPurchaseReceiveBillOrderItem> orderDetails;
}
