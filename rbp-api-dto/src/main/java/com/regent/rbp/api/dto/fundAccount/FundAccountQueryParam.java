package com.regent.rbp.api.dto.fundAccount;

import com.regent.rbp.api.dto.channel.Channelorganization;
import lombok.Data;

/**
 * @Description 资金号查询参数
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Data
public class FundAccountQueryParam {
    private String[] code;
    private String[] name;
    private String[] parentCode;
    private String[] legalPerson;
    private String[] taxNumber;
    private Integer[] type;
    private Integer[] status;
    private String createdDateStart;
    private String createdDateEnd;
    private String checkDateStart;
    private String checkDateEnd;
    private String updatedDateStart;
    private String updatedDateEnd;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;
    private Channelorganization channelorganization;
}
