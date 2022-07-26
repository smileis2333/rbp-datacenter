package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.SalePlanQueryParam;
import com.regent.rbp.api.dto.sale.SalePlanSaveParam;
import com.regent.rbp.api.dto.sale.SalesPlanBillQueryResult;
import com.regent.rbp.api.service.sale.SalesPlanBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiConstants.API_SALES_PLAN)
@Api(tags = "销售计划")
public class SalesPlanController {
    @Autowired
    private SalesPlanBillService salesPlanBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<SalesPlanBillQueryResult> query(@RequestBody SalePlanQueryParam param) {
        PageDataResponse<SalesPlanBillQueryResult> result = salesPlanBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid SalePlanSaveParam param) {
        DataResponse result = salesPlanBillService.save(param);
        return result;
    }
}
