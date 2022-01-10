package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.SaleOrderQueryParam;
import com.regent.rbp.api.dto.sale.SaleOrderSaveParam;
import com.regent.rbp.api.dto.sale.SalesOrderBillQueryResult;
import com.regent.rbp.api.service.sale.SalesOrderBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: rbp-datacenter
 * @description: 销售单
 * @author: HaiFeng
 * @create: 2021-11-10 13:42
 */
@RestController
@RequestMapping(ApiConstants.API_SALES_ORDER)
@Api(tags = "销售单")
public class SalesOrderController {

    @Autowired
    SalesOrderBillService salesOrderBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<SalesOrderBillQueryResult> query(@RequestBody SaleOrderQueryParam param) {
        PageDataResponse<SalesOrderBillQueryResult> result = salesOrderBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody SaleOrderSaveParam param) {
        DataResponse result = salesOrderBillService.save(param);
        return result;
    }
}
