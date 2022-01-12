package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillSaveParam;
import com.regent.rbp.api.service.purchase.PurchaseReceiveBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_PURCHASE_RECEIVE_BILL)
@Api(tags = "采购入库单")
public class PurchaseReceiveBillController {

    @Autowired
    private PurchaseReceiveBillService purchaseReceiveBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseReceiveBillQueryResult> query(@RequestBody @Valid PurchaseReceiveBillQueryParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new PageDataResponse<>(0, Collections.emptyList());
        }
        return purchaseReceiveBillService.query(param);
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid PurchaseReceiveBillSaveParam param) {
        return purchaseReceiveBillService.save(param);
    }

}
