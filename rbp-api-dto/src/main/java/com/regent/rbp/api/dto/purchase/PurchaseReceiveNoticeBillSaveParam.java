package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.BoxDetailData;
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

    @NotBlank
    private String moduleId;

    @NotBlank
    @ConflictManualIdCheck(targetTable = "rbp_purchase_receive_notice_bill")
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

    @NotNull
    @BillStatus
    private Integer status;

    @BillNo(targetTable = "rbp_purchase_bill")
    private String purchaseNo;

    private BigDecimal taxRate;
    @CurrencyTypeCheck
    private String currencyType;
    private String notes;
    @Valid
    private List<CustomizeDataDto> customizeData;

    @Valid
    @NotEmpty
    @GoodsInfo
    private List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData;

    @Valid
    private List<BoxDetailData> boxDetailData;
}
