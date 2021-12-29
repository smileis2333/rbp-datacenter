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
public class PurchaseReceiveNoticeBillSaveParam {

    @NotBlank(message = "{javax.NotBlank.moduleId}")
    private String moduleId;

    @NotBlank(message = "{javax.NotBlank.manualId}")
    @ConflictManualIdCheck(targetClass = "com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill", message = "{regent.conflictManualIdCheck}")
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

    @NotNull(message = "{javax.NotBlank.status}")
    @BillStatus(message = "{regent.billStatus.statusNotExist}")
    private Integer status;

    @PurchaseNo
    private String purchaseNo;
    private BigDecimal taxRate;
    @CurrencyTypeCheck(message = "{regent.currencyTypeCheck.currencyTypeNotExist}")
    private String currencyType;
    private String notes;
    private List<CustomizeDataDto> customizeData;
    @Valid
    @NotEmpty(message =  "{javax.NotEmpty.goodsDetailData}")
    private List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData;

    public void setGoodsDetailData(List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData) {
        int i = 1;
        for (PurchaseReceiveNoticeBillGoodsDetailData goodsDetailDatum : goodsDetailData) {
            goodsDetailDatum.setIdx(i++);
        }
        this.goodsDetailData = goodsDetailData;
    }
}
