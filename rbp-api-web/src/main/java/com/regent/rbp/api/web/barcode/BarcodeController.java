package com.regent.rbp.api.web.barcode;

import com.regent.rbp.api.dto.base.BarcodeQueryParam;
import com.regent.rbp.api.dto.base.BarcodeQueryResult;
import com.regent.rbp.api.dto.base.BarcodeSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.barcode.BarcodeService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_BARCODE)
@Api(tags = "条形码档案")
public class BarcodeController {
    @Autowired
    private BarcodeService barcodeService;

    @PostMapping("/query")
    public PageDataResponse<BarcodeQueryResult> query(@RequestBody @Valid BarcodeQueryParam param) {
        return barcodeService.searchPageData(param);
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody @Valid BarcodeSaveParam param) {
        return barcodeService.batchCreate(param);
    }

}


