package com.regent.rbp.api.service.purchase.context;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class PurchaseReceiveBillQueryContext {
    private String moduleId;
    private String billNo;
    private Date billDate;
    private List<Long> businessTypeIds;
    private List<Long> supplierIds;
    private List<Long> toChannelIds;
    private String manualId;
    private List<Long> currencyTypeIds;
    private String notes;
    private List<Integer> status;
    private Integer pageNo;
    private Integer pageSize;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
}
