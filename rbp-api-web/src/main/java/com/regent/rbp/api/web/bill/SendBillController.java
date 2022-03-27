package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillSaveParam;
import com.regent.rbp.api.service.send.SendBillService;
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
 * @description: 发货单
 * @author: chenchengui
 * @create: 2021-12-16
 */
@RestController
@RequestMapping(ApiConstants.API_SEND_BILL)
@Api(tags = "发货单")
public class SendBillController {

    @Autowired
    private SendBillService sendBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<SendBillQueryResult> query(@RequestBody SendBillQueryParam param) {
        PageDataResponse<SendBillQueryResult> result = sendBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid SendBillSaveParam param) {
        DataResponse result = sendBillService.save(param);
        return result;
    }
}
