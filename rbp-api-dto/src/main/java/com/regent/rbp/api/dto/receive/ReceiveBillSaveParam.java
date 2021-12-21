package com.regent.rbp.api.dto.receive;

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
public class ReceiveBillSaveParam {
    private String moduleId;
    private String manualId;
    private Date billDate;
    private String businessType;
    private String channelCode;
    private String toChannelCode;
    private String currencyType;
    private String notes;
    private String sendNo;
    private String noticeNo;
    private Integer status;
    private List<CustomizeDataDto> customizeData;
    private List<ReceiveBillGoodsDetailData> goodsDetailData;
    private Long baseBusinessTypeId;

    public String getNoticeNo() {
        if (baseBusinessTypeId != null && baseBusinessTypeId == 1100000000000023l) {
            return noticeNo;
        }
        return null;
    }
}
