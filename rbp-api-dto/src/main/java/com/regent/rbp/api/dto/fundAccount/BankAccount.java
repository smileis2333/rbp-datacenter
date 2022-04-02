package com.regent.rbp.api.dto.fundAccount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 银行账户
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class BankAccount {
    @ApiModelProperty("开户行")
    private String accountBank;

    @ApiModelProperty("开户名")
    private String bankName;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("银行账款科目")
    private String bankSubject;
}
