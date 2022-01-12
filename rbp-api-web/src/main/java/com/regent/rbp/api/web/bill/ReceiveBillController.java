package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.dto.receive.ReceiveBillSaveParam;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_RECEIVE_BILL)
@Api(tags = "收货单")
public class ReceiveBillController {
    @Autowired
    private ReceiveBillService sendBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<ReceiveBillQueryResult> query(@RequestBody ReceiveBillQueryParam param) {
        PageDataResponse<ReceiveBillQueryResult> result = sendBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody ReceiveBillSaveParam param) {
        DataResponse result = sendBillService.save(param);
        return result;
    }

}
