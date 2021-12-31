package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.purchaseReturn.PurchaseReturnNoticeBillService;
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
 * @description: 采购退货通知
 * @author: HaiFeng
 * @create: 2021/12/30 14:00
 */
@RestController
@RequestMapping(ApiConstants.API_PURCHASE_RETURN_NOTICE_BILL)
@Api(tags = "采购退货通知")
public class PurchaseReturnNoticeBillController {

    @Autowired
    PurchaseReturnNoticeBillService purchaseReturnNoticeBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseReturnNoticeBillQueryResult> query(@RequestBody PurchaseReturnNoticeBillQueryParam param) {
        PageDataResponse<PurchaseReturnNoticeBillQueryResult> result = purchaseReturnNoticeBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public DataResponse save(@RequestBody PurchaseReturnNoticeBillSaveParam param) {
        DataResponse result = purchaseReturnNoticeBillService.save(param);
        return result;
    }

}
