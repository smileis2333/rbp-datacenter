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
    private FundAccountPricePolicy fundAccountPricePolicy;
    private FundAccountBrandPricePolicy fundAccountBrandPricePolicy;
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

        if(null != param.getOrganization()){
            if((Object)param.getOrganization().getOrganization1() instanceof Long){
                this.fundAccount.setOrganization1(Long.valueOf(param.getOrganization().getOrganization1()));
            }
            if((Object)param.getOrganization().getOrganization2() instanceof Long){
                this.fundAccount.setOrganization2(Long.valueOf(param.getOrganization().getOrganization2()));
            }
            if((Object)param.getOrganization().getOrganization3() instanceof Long){
                this.fundAccount.setOrganization3(Long.valueOf(param.getOrganization().getOrganization3()));
            }
            if((Object)param.getOrganization().getOrganization4() instanceof Long){
                this.fundAccount.setOrganization4(Long.valueOf(param.getOrganization().getOrganization4()));
            }
            if((Object)param.getOrganization().getOrganization5() instanceof Long){
                this.fundAccount.setOrganization5(Long.valueOf(param.getOrganization().getOrganization5()));
            }
        }
        //自定义字段
        if(CollUtil.isNotEmpty(param.getCustomizeData())){
            this.customizeData = param.getCustomizeData().stream().collect(Collectors.toMap(CustomizeDataDto::getCode,x->x.getValue()));
        }
    }

}
