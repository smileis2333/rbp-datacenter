package com.regent.rbp.api.web.label;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.label.LabelQueryParam;
import com.regent.rbp.api.dto.label.LabelSaveParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;
import com.regent.rbp.api.service.label.LabelService;
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
@RequestMapping(ApiConstants.API_LABEL)
@Api(tags = "唯一码档案")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseReturnBillQueryResult> query(@RequestBody LabelQueryParam param) {
        return labelService.query(param);
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid LabelSaveParam param) {
        return labelService.save(param);
    }
}
