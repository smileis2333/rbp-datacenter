package com.regent.rbp.api.service.sale;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.SaleOrderQueryParam;
import com.regent.rbp.api.dto.sale.SaleOrderSaveParam;
import com.regent.rbp.api.dto.sale.SalesOrderBillQueryResult;

/**
 * @program: rbp-datacenter
 * @description: 销售单
 * @author: HaiFeng
 * @create: 2021-11-08 13:34
 */
public interface SalesOrderBillService {

    PageDataResponse<SalesOrderBillQueryResult> query(SaleOrderQueryParam param);

    DataResponse save(SaleOrderSaveParam param);
}
