package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillQueryResult {
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private String businessType;
    private String manualId;
    private String channelCode;
    private String toChannelCode;
    @JsonIgnore
    private String channelName;
    private String currencyType;
    private String notes;
    private String sendNo;
    private String noticeNo;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    private List<CustomizeDataDto> customizeData;
    private List<ReceiveBillGoodsDetailData> goodsDetailData;

}
