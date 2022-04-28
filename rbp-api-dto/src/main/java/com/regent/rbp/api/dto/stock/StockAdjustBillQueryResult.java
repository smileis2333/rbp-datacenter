package com.regent.rbp.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class StockAdjustBillQueryResult {
    @JsonIgnore
    private Long id;
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String businessType;
    private String manualId;
    private String channelCode;
    private String notes;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;

    private List<CustomizeDataDto> customizeData;

    private List<StockAdjustBillGoodsDetailData> goodsDetailData;
}
