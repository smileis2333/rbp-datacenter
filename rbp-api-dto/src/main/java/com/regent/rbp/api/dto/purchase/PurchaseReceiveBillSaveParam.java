package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class PurchaseReceiveBillSaveParam {

    @NotBlank(message = "{javax.NotBlank.moduleId}")
    private String moduleId;

    @NotBlank(message = "{javax.NotBlank.manualId}")
    @ConflictManualIdCheck(targetClass = "com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBill", message = "{regent.conflictManualIdCheck}")
    private String manualId;

    @NotNull(message = "{javax.NotNull.billDate}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotBlank(message = "{javax.NotBlank.businessType}")
    @BusinessTypeCheck(message = "{regent.businessTypeCheck.businessTypeNotExist}")
    private String businessType;

    @NotBlank(message = "{javax.NotBlank.supplierCode}")
    @SupplierCodeCheck(message = "{regent.supplierCodeCheck.supplierCodeNotExist}")
    private String supplierCode;

    @NotBlank(message = "{javax.NotBlank.toChannelCode}")
    @ChannelCodeCheck(message = "{regent.channelCodeCheck.toChannelCodeNotExist}")
    private String toChannelCode;

    @PurchaseNo
    private String purchaseNo;

    @PurchaseReceiveNoticeNo
    private String noticeNo;

    private BigDecimal taxRate;

    @CurrencyTypeCheck(message = "{regent.currencyTypeCheck.currencyTypeNotExist}")
    private String currencyType;

    private String notes;

    @NotNull(message = "{javax.NotBlank.status}")
    @BillStatus(message = "{regent.billStatus.statusNotExist}")
    private Integer status;

    private List<CustomizeDataDto> customizeData;
    @Valid
    @NotEmpty(message = "{javax.NotEmpty.goodsDetailData}")
    private List<PurchaseReceiveBillGoodsDetailData> goodsDetailData;

    public void setGoodsDetailData(List<PurchaseReceiveBillGoodsDetailData> goodsDetailData) {
        int i = 1;
        for (PurchaseReceiveBillGoodsDetailData goodsDetailDatum : goodsDetailData) {
            goodsDetailDatum.setIdx(i++);
        }
        this.goodsDetailData = goodsDetailData;
    }
}
