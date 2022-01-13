package com.regent.rbp.api.service.fundAccount.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.fundAccount.FundAccountBank;
import com.regent.rbp.api.core.fundAccount.FundAccountBrandPricePolicy;
import com.regent.rbp.api.core.fundAccount.FundAccountPricePolicy;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.fundAccount.FundAccountSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Data
public class FundAccountSaveContext {
    private FundAccount fundAccount;
    private List<FundAccountPricePolicy> fundAccountPricePolicies = new ArrayList<>();
    private List<FundAccountBrandPricePolicy> fundAccountBrandPricePolicies = new ArrayList<>();
    private FundAccountBank fundAccountBank;
    private Map customizeData;

    public FundAccountSaveContext( FundAccountSaveParam param) {
        this.fundAccount = new FundAccount();
        Long userId = ThreadLocalGroup.getUserId();
        this.fundAccount.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.fundAccount.setCreatedBy(userId);
        this.fundAccount.setUpdatedBy(userId);

        if(param != null) {
            this.readProperties(param);
        }
    }

    private void readProperties(FundAccountSaveParam param) {
        this.fundAccount.setCode(param.getCode());
        this.fundAccount.setName(param.getName());
        this.fundAccount.setNotes(param.getNotes());
        //this.fundAccount.getParentId(param.getParentCode());
        this.fundAccount.setLegalPerson(param.getLegalPerson());
        this.fundAccount.setCredit(param.getCredit());
        this.fundAccount.setTaxRate(param.getTaxRate());
        this.fundAccount.setTaxNumber(param.getTaxNumber());
        this.fundAccount.setType(param.getType());
        this.fundAccount.setType(param.getType());
        this.fundAccount.setStatus(param.getStatus());
        //自定义字段
        if(CollUtil.isNotEmpty(param.getCustomizeData())){
            this.customizeData = param.getCustomizeData().stream().collect(Collectors.toMap(CustomizeDataDto::getCode,x->x.getValue()));
        }
    }

}
