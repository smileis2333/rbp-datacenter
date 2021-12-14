package com.regent.rbp.api.core.fundAccount;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 资金号银行账号 对象 rbp_fund_account_bank
 * 
 * @author liuzhicheng
 * @date 2020-09-04
 */
@Data
@ApiModel(description="资金号银行账号 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_fund_account_bank")
public class FundAccountBank extends Model<FundAccountBank> {

    @ApiModelProperty(notes = "编码")
    private Long id;


    @ApiModelProperty(notes = "资金号")
    private Long fundAccountId;


    @ApiModelProperty(notes = "开户行")
    private String accountBank;


    @ApiModelProperty(notes = "开户名称")
    private String accountBankName;


    @ApiModelProperty(notes = "账号")
    private String account;


    @ApiModelProperty(notes = "银行账款科目")
    private String bankSubject;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }
}
