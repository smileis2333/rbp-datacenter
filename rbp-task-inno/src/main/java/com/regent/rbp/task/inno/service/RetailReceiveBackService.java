package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.UpdateRetailReceiveBackByStatusParam;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货单 Service
 * @author: HaiFeng
 * @create: 2021-10-14 17:52
 */
public interface RetailReceiveBackService {

    void UpdateReturnOrderStatus(UpdateRetailReceiveBackByStatusParam param);

}
