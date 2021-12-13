package com.regent.rbp.api.web.supplier;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.supplier.SupplierQueryParam;
import com.regent.rbp.api.dto.supplier.SupplierQueryResult;
import com.regent.rbp.api.dto.supplier.SupplierSaveParam;
import com.regent.rbp.api.service.supplier.SupplierService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.API_SUPPLIER)
@Api(tags = "供应商档案")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping("/query")
    public PageDataResponse<SupplierQueryResult> query(@RequestBody SupplierQueryParam param) {
        PageDataResponse<SupplierQueryResult> result = supplierService.query(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody SupplierSaveParam param) {
        DataResponse result = supplierService.save(param);
        return result;
    }

}
