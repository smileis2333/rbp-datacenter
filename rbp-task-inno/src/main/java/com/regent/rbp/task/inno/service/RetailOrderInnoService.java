package com.regent.rbp.task.inno.service;

import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 全渠道订单
 * @author: HaiFeng
 * @create: 2022/4/8 9:34
 */
public interface RetailOrderInnoService {

    Map<String, String> getOrderStatus(String eorderid, String barcode);
}
