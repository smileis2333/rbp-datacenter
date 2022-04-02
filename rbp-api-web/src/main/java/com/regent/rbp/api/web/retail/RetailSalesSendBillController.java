package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillSaveParam;
import com.regent.rbp.api.service.retail.RetailSalesSendBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@RestController
@RequestMapping(ApiConstants.API_SALES_SEND)
@Api(tags = "全渠道发货单")
public class RetailSalesSendBillController {

    @Autowired
    private RetailSalesSendBillService retailSalesSendBillService;

    @PostMapping
    public ModelDataResponse<String> save(@RequestBody RetailSalesSendBillSaveParam param) {
        return retailSalesSendBillService.save(param);
    }
}
