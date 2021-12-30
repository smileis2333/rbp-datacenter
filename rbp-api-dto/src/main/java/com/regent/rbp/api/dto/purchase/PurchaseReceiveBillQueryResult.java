package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@Data
public class PurchaseReceiveBillQueryResult {
    private String moduleId;
    private String billNo;
    private String manualId;
    private Date billDate;
    private String businessType;
    private String supplierCode;
    private String toChannelCode;
    private String currencyType;
    private BigDecimal taxRate;
    private String notes;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    private List<CustomizeDataDto> customizeData;
    private List<PurchaseReceiveBillGoodsDetailData>goodsDetailData;
}
