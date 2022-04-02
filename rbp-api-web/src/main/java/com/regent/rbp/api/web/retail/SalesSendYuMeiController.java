package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiSaveParam;
import com.regent.rbp.api.service.retail.SalesSendYuMeiService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-04-01
 * @Description
 */
@RestController
@RequestMapping(ApiConstants.API_SALES_SEND_YUMEI)
@Api(tags = "全渠道发货单(玉美)")
public class SalesSendYuMeiController {

    @Autowired
    private SalesSendYuMeiService salesSendYuMeiService;

    @PostMapping
    public ModelDataResponse<String> save(@RequestBody SalesSendYuMeiSaveParam param) {
        return salesSendYuMeiService.save(param);
    }
}
