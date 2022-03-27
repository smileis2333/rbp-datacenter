package com.regent.rbp.api.dto.purchase;

import com.regent.rbp.api.dto.validate.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;


@Data
public class PurchaseReturnBillSaveParam extends BaseBillSaveParam {
    @NotBlank
    @ConflictManualIdCheck(targetTable = "rbp_purchase_return_bill")
    private String manualId;

    @NotBlank
    @BusinessTypeCheck
    private String businessType;

    @NotBlank
    @SupplierCodeCheck
    private String supplierCode;

    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    @BillNo(targetTable = "rbp_purchase_bill")
    private String purchaseNo;

    @BillNo(targetTable = "rbp_purchase_return_notice_bill")
    private String noticeNo;

    private BigDecimal taxRate;

    @CurrencyTypeCheck
    private String currencyType;

    @Valid
    @NotEmpty
    @GoodsInfo
    private List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData;

}
