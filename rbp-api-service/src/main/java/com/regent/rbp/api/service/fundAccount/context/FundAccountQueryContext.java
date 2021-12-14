package com.regent.rbp.api.service.fundAccount.context;

import com.regent.rbp.api.dto.channel.Channelorganization;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Data
public class FundAccountQueryContext {
    private String[] code;
    private String[] name;
    private List<Long> parentId;
    private String[] taxNumber;
    private Integer[] type;
    private Integer[] status;
    private String[] legalPerson;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;
    private Channelorganization channelorganization;
}
