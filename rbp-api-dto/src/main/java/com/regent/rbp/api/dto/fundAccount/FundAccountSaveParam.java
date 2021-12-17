package com.regent.rbp.api.dto.fundAccount;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.Channelorganization;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 资金号保存参数
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Data
public class FundAccountSaveParam {
    private String code;
    private String name;
    private String notes;
    private String parentCode;
    private String legalPerson;
    private BigDecimal credit;
    private BigDecimal taxRate;
    private String taxNumber;
    private Integer type;

    private Channelorganization organization;
    private PricePolicy pricePolicy;
    private BrandPricePolicy brandPricePolicy;
    private BankAccount bankAccount;
    private List<CustomizeDataDto> customizeData;
}