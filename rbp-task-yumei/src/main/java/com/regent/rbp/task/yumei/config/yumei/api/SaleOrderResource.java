package com.regent.rbp.task.yumei.config.yumei.api;

import com.regent.rbp.task.yumei.config.yumei.YumeiResouceClientConfiguration;
import com.regent.rbp.task.yumei.model.YumeiOfflineSaleOrderPayload;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryPageResp;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author huangjie
 * @date : 2022/04/29
 * @description
 */
@Validated
@FeignClient(value = "saleOrderResource", url = "${yumei.url:undefined}",configuration = YumeiResouceClientConfiguration.class)
public interface SaleOrderResource {

    @RequestMapping(method = RequestMethod.POST, value = "/api/trade/orderQuery")
    YumeiOrderQueryPageResp orderQuery(@RequestBody YumeiOrderQueryReq param);

    @RequestMapping(method = RequestMethod.POST, value = "/api/offshop/tradeCreate")
    Void createOfflineSaleOrder(@RequestBody YumeiOfflineSaleOrderPayload payload);
}
