package com.regent.rbp.task.yumei.controller;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.task.yumei.model.YumeiReceiptBillSaveParam;
import com.regent.rbp.task.yumei.service.impl.YumeiReceiptBillServiceBean;
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
 * @date : 2022/05/06
 * @description
 */
@RestController
@RequestMapping("api/yumei/receipt")
@Api(tags = "收款单")
public class YumeiReceiptBillController {
    @Autowired
    private YumeiReceiptBillServiceBean receiptBillService;

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid YumeiReceiptBillSaveParam param) {
        DataResponse result = receiptBillService.customSave(param);
        return result;
    }
}
