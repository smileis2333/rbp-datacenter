package com.regent.rbp.task.yumei.config.yumei.api;

import com.regent.rbp.task.yumei.config.yumei.YumeiResouceClientConfiguration;
import com.regent.rbp.task.yumei.model.YumeiReturnOrderCreatePayload;
import com.regent.rbp.task.yumei.model.YumeiReturnOrderValidatedPayload;
import com.regent.rbp.task.yumei.model.YumeiTransferOrderCreatePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Validated
@FeignClient(value = "offShopResource", url = "${yumei.url:undefined}",configuration = YumeiResouceClientConfiguration.class)
public interface OffShopResource {
    /**
     * 退仓订单_创建
     * @param payload
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/offshop/returnOrderCreate")
    Void returnOrderCreate(@RequestBody @Valid YumeiReturnOrderCreatePayload payload);

    @RequestMapping(method = RequestMethod.POST, value = "/api/offshop/returnOrderValidated")
    Void returnOrderValidated(@RequestBody @Valid YumeiReturnOrderValidatedPayload payload);

    @RequestMapping(method = RequestMethod.POST, value = "/api/offshop/transferOrderCreate")
    Void transferOrderCreate(@RequestBody @Valid YumeiTransferOrderCreatePayload payload);
}
