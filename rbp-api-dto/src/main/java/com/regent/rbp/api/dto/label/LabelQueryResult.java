package com.regent.rbp.api.dto.label;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangjie
 * @date : 2022/02/15
 * @description
 */
@Data
public class LabelQueryResult {
    private String code;
    private String batchManagementNo;
    private String channelCode;
    private String moduleNo;
    private String billNo;
    private String labelRuleNo;
    private Integer status;
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    private BigDecimal quantity;
    private String notes;
    private Date createdTime;
    private Date updatedTime;
}
