package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class PurchaseReturnBillQueryParam {
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private List<String> businessType;
    private String manualId;
    private List<String> supplierCode;
    private List<String> channelCode;
    private List<String> currencyType;
    private String notes;
    private List<Integer> status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDateEnd;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;
}
