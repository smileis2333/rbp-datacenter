package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class PurchaseReturnBillQueryResult {
    private Long id;
    private String code;
    private String message;
    private String totalCount;
    private String data;
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String businessType;
    private String supplierCode;
    private String channelCode;
    private String manualId;
    private String currencyType;
    private String taxRate;
    private String notes;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    private List<CustomizeDataDto> customizeData;
    private List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData;
}
