package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailOrderBillUpdateParam;
import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenchungui
 * @date 2021-09-14
 */
@RestController
@RequestMapping(ApiConstants.API_RETAIL_ORDER)
@Api(tags = "全渠道订单")
public class RetailOrderBillController {

    @Autowired
    private RetailOrderBillService retailOrderBillService;

    @PostMapping("/save")
    public ModelDataResponse<String> save(@RequestBody RetailOrderBillSaveParam param) {
        ModelDataResponse<String> result = retailOrderBillService.save(param);
        return result;
    }

    @PutMapping("/updateStatus")
    public DataResponse update(@RequestBody RetailOrderBillUpdateParam param) {
        DataResponse result = retailOrderBillService.updateStatus(param);
        return result;
    }

}
