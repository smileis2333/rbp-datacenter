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

    @NotBlank
    private String moduleId;

    @NotBlank
    @ConflictManualIdCheck(targetTable = "rbp_purchase_receive_bill")
    private String manualId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotBlank
    @BusinessTypeCheck
    private String businessType;

    @NotBlank
    @SupplierCodeCheck
    private String supplierCode;

    @NotBlank
    @ChannelCodeCheck
    private String toChannelCode;

    @BillNo(targetTable = "rbp_purchase_bill")
    private String purchaseNo;

    @BillNo(targetTable = "rbp_notice_bill")
    private String noticeNo;

    private BigDecimal taxRate;

    @CurrencyType
    private String currencyType;

    private String notes;

    @NotNull
    @BillStatus
    private Integer status;

    private List<CustomizeDataDto> customizeData;

    @Valid
    @NotEmpty
    @GoodsInfo
    private List<PurchaseReceiveBillGoodsDetailData> goodsDetailData;

}
