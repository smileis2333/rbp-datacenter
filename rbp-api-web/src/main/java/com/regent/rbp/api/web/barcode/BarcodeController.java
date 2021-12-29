package com.regent.rbp.api.web.barcode;

import com.regent.rbp.api.dto.base.BarcodeQueryResult;
import com.regent.rbp.api.dto.base.BarcodeQueryParam;
import com.regent.rbp.api.dto.base.BarcodeSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.barcode.BarcodeService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public PageDataResponse<BarcodeQueryResult> query(@RequestBody @Valid BarcodeQueryParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new PageDataResponse<>(0, Collections.emptyList());
        }
        PageDataResponse<BarcodeQueryResult> result = barcodeService.searchPageData(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody @Valid BarcodeSaveParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String messages = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            return ModelDataResponse.errorParameter(messages);
        }
        DataResponse result = barcodeService.batchCreate(param);
        return result;
    }


}


