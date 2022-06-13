package com.regent.rbp.api.dto.purchase;

import com.regent.rbp.api.dto.validate.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;


@BusinessBill(baseModuleId = "700016",baseTable = "rbp_purchase_receive_bill")
@Data
public class PurchaseReceiveBillSaveParam extends AbstractBusinessBillSaveParam{

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

    @Valid
    @NotEmpty
    @GoodsInfo
    private List<PurchaseReceiveBillGoodsDetailData> goodsDetailData;

}
