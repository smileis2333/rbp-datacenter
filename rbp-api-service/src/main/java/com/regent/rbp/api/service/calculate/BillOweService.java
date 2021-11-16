package com.regent.rbp.api.service.calculate;

import com.regent.rbp.api.dto.calculate.SalePlanBillOweDetail;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 存储过程处理
 * @author: HaiFeng
 * @create: 2021-11-15 16:07
 */
public interface BillOweService {

    List<SalePlanBillOweDetail> calculateSalePlanBillOwe(Long channelId, String channelTable, Long goodsId, String goodsTable, Long salePlanId);

}
