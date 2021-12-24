package com.regent.rbp.api.service.purchase.context;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class PurchaseReceiveNoticeBillQueryContext {
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
