package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class PurchaseReceiveNoticeBillSaveParam {
    private String moduleId;
    private String manualId;
    private Date billDate;
    private String businessType;
    private String supplierCode;
    private String toChannelCode;
    private String purchaseNo;
    private BigDecimal taxRate;
    private String currencyType;
    private String notes;
    private Integer status;
    private List<CustomizeDataDto> customizeData;
    private List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData;
}
