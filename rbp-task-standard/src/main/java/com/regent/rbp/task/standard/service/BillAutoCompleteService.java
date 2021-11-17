package com.regent.rbp.task.standard.service;

import com.regent.rbp.task.standard.module.param.BillParam;

/**
 * @program: rbp-datacenter
 * @description: 单据自动完成
 * @author: HaiFeng
 * @create: 2021-11-15 14:49
 */
public interface BillAutoCompleteService {


    void salePlanBillAutoComplete(BillParam billParam);

    void noticeBillAutoComplete(BillParam billParam);

    void purchaseBillAutoComplete(BillParam billParam);
}
