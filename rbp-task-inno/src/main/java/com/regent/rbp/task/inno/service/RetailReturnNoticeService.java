package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知
 * @author: HaiFeng
 * @create: 2021-09-26 11:15
 */
public interface RetailReturnNoticeService {

    void downloadRetailReturnNoticeList(RetailReturnNoticeParam param);
}
