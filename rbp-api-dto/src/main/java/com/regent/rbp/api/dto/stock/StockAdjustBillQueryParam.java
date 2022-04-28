package com.regent.rbp.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.DefaultParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class StockAdjustBillQueryParam extends DefaultParam {
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String businessType;
    private String manualId;
    private String channelCode;
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
}
