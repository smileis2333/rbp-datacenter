package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReceiveBackBillSaveParam;
import com.regent.rbp.api.service.retail.RetailReceiveBackBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-04-03
 * @Description
 */
@RestController
@RequestMapping(ApiConstants.API_RETAIL_RECEIVE_BACK)
@Api(tags = "全渠道收退货单")
public class RetailReceiveBackBillController {

    @Autowired
    private RetailReceiveBackBillService retailReceiveBackBillService;

    @PostMapping("/save")
    public ModelDataResponse<String> save(@RequestBody RetailReceiveBackBillSaveParam param) {
        return retailReceiveBackBillService.save(param);
    }
}
