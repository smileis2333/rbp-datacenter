package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseBillSaveParam;
import com.regent.rbp.api.service.purchase.PurchaseBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: rbp-datacenter
 * @description: 采购订单
 * @author: chenchengui
 * @create: 2021-12-21
 */
@RestController
@RequestMapping(ApiConstants.API_PURCHASE_BILL)
@Api(tags = "采购订单")
public class PurchaseBillController {

    @Autowired
    private PurchaseBillService purchaseBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseBillQueryResult> query(@RequestBody PurchaseBillQueryParam param) {
        PageDataResponse<PurchaseBillQueryResult> result = purchaseBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid PurchaseBillSaveParam param) {
        DataResponse result = purchaseBillService.save(param);
        return result;
    }
}
