package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class PurchaseReceiveNoticeBillQueryParam {
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private List<String> businessType;
    private List<String> supplierCode;
    private List<String> toChannelCode;
    private String manualId;
    private List<String> currencyType;
    private String notes;
    private List<Integer> status;
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
