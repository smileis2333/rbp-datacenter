package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.validate.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@Data
public class PurchaseReceiveBillQueryParam {
    @BusinessTypeCheck
    private List<String> businessType;
    @SupplierCodeCheck
    private List<String> supplierCode;
    @ChannelCodeCheck
    private List<String> toChannelCode;
    @CurrencyTypeCheck
    private List<String> currencyType;
    @BillStatus
    private List<Integer> status;
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String manualId;
    private String notes;
    private Integer pageNo;
    private Integer pageSize;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;
}