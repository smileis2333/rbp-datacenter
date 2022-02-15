package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillSaveParam;
import com.regent.rbp.api.service.purchaseReturn.PurchaseReturnBillService;
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
 * @author huangjie
 * @date : 2022/2/14
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_PURCHASE_RETURN_BILL)
@Api(tags = "采购退货单")
public class PurchaseReturnBillController {

    @Autowired
    private PurchaseReturnBillService purchaseReturnBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseReturnBillQueryResult> query(@RequestBody PurchaseReturnBillQueryParam param) {
        return purchaseReturnBillService.query(param);
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid PurchaseReturnBillSaveParam param) {
        return purchaseReturnBillService.save(param);
    }
}
