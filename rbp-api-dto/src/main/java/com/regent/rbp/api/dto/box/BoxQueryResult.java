package com.regent.rbp.api.dto.box;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
public class BoxQueryResult {
    private Long id;
    private String code;
    private String manualId;
    private String batchNumber;
    private String channelCode;
    private String supplierCode;
    private String distributionTypeCode;
    private Integer type;
    private Integer status;
    private Date buildDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private Integer validateDay;
    private String notes;
    private BigDecimal weight;
    private List<BoxDetailItem> boxDetail;

}
