package com.regent.rbp.api.dto.fundAccount;

import lombok.Data;

/**
 * @Description 银行账户
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class BankAccount {
    private String accountBank;
    private String bankName;
    private String account;
    private String bankSubject;
}
