package com.regent.rbp.api.service.receive.context;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillQueryContext {
    private String moduleId;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private List<Long> businessTypeIds;
    private String manualId;
    private List<Long> channelIds;
    private List<Long> toChannelIds;
    private List<Long> currencyTypeIds;
    private String notes;
    private Long sendId;
    private Long noticeId;
    private List<Integer> status;
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
    private String fields;
    private Integer pageNo;
    private Integer pageSize;

    private boolean valid = true;

    public void setBusinessTypeIds(List<Long> businessTypeIds) {
        if (businessTypeIds.isEmpty())
            valid = false;
        this.businessTypeIds = businessTypeIds;
    }

    public void setChannelIds(List<Long> channelIds) {
        if (channelIds.isEmpty())
            valid = false;
        this.channelIds = channelIds;
    }

    public void setToChannelIds(List<Long> toChannelIds) {
        if (channelIds.isEmpty())
            valid = false;
        this.toChannelIds = toChannelIds;
    }

    public void setCurrencyTypeIds(List<Long> currencyTypeIds) {
        if (channelIds.isEmpty())
            valid = false;
        this.currencyTypeIds = currencyTypeIds;
    }

    public void setSendId(Long sendId) {
        if (sendId==null)
            valid = false;
        this.sendId = sendId;
    }

    public void setNoticeId(Long noticeId) {
        if (sendId==null)
            valid = false;
        this.noticeId = noticeId;
    }
}
