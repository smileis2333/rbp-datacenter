package com.regent.rbp.api.dto.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class ReceiptBillQueryResult{
    @JsonIgnore
    private Long billId;
    private String moduleId;
    private String manualId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String channelCode;
    private String fundAccountCode;
    private String fundAccountBank;
    private String receiptAccount;
    private String receiptType;
    private String currencyType;
        private BigDecimal exchangeRate;
    private BigDecimal currencyAmount;
    private BigDecimal amount;
    private String notes;
    private Integer status;
    private Date createdTime;
    private Date updatedTime;
    private Date checkTime;
    private List<CustomizeDataDto> customizeData;
}
