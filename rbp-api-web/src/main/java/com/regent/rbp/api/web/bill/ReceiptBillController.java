package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryParam;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryResult;
import com.regent.rbp.api.dto.receipt.ReceiptBillSaveParam;
import com.regent.rbp.api.dto.validate.group.Standard;
import com.regent.rbp.api.service.receipt.ReceiptBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_RECEIPT)
@Api(tags = "收款单")
public class ReceiptBillController {
    @Autowired
    @Qualifier(value = "receiptBillServiceBean")
    private ReceiptBillService receiptBillService;

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Validated({Standard.class, Default.class}) ReceiptBillSaveParam param) {
        DataResponse result = receiptBillService.save(param);
        return result;
    }

    @ApiOperation(value = "查询")
    @PostMapping("query")
    public PageDataResponse<ReceiptBillQueryResult> query(@RequestBody ReceiptBillQueryParam param) {
        return receiptBillService.query(param);
    }
}
