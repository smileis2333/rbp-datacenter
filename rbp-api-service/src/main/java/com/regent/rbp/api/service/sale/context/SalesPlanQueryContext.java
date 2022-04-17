package com.regent.rbp.api.service.sale.context;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SalesPlanQueryContext {
    private String moduleId;
    private String billNo;
    private Date billDate;
    private Long businessTypeId;
    private Long channelId;
    private String manualId;
    private Long priceTypeId;
    private Long currencyTypeId;
    private String notes;
    private List<Integer> status;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private List<String> fields;
    private int pageNo;
    private int pageSize;

}
