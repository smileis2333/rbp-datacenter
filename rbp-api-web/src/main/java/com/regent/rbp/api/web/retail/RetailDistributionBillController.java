package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailDistributionBillSaveParam;
import com.regent.rbp.api.service.retail.RetailDistributionBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-03-31
 * @Description
 */
@RestController
@RequestMapping(ApiConstants.API_RETAIL_DISTRIBUTION)
@Api(tags = "全渠道配货单")
public class RetailDistributionBillController {

    @Autowired
    private RetailDistributionBillService retailDistributionBillService;

    @PostMapping("/save")
    public ModelDataResponse<String> save(@RequestBody RetailDistributionBillSaveParam param) {
        return retailDistributionBillService.save(param);
    }
}
