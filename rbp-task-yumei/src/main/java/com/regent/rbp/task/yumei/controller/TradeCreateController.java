package com.regent.rbp.task.yumei.controller;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.infrastructure.request.ListRequest;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-04-07
 * @Description
 */
@RestController
@RequestMapping("api/trade/tradeCreate")
@Api(tags = "玉美-订单推送")
public class TradeCreateController {

    @Autowired
    private SaleOrderService saleOrderService;

    @PostMapping
    public ModelDataResponse<String> save(@RequestBody ListRequest<String> param) {
        saleOrderService.pushOrderToYuMei(param.getData());
        return ModelDataResponse.Success("ok");
    }

}
