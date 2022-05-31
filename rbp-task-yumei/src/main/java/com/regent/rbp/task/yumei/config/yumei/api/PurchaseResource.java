package com.regent.rbp.task.yumei.config.yumei.api;

import com.regent.rbp.task.yumei.config.yumei.YumeiResouceClientConfiguration;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryPageResp;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryReq;
import com.regent.rbp.task.yumei.model.YumeiPurchaseReceiveOrderPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author huangjie
 * @date : 2022/05/23
 * @description
 */
@Validated
@FeignClient(value = "purchaseResource", url = "${yumei.url:undefined}",configuration = YumeiResouceClientConfiguration.class)
public interface PurchaseResource {
    @RequestMapping(method = RequestMethod.POST, value = "/api/trade/orderQuery")
    YumeiOrderQueryPageResp orderQuery(@RequestBody YumeiOrderQueryReq param);

    /**
     * 创建采购入库单
     * @param payload
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/offshop/purchaseStockinOrderCreate")
    Void createPurchaseReceive(@RequestBody @Valid YumeiPurchaseReceiveOrderPayload payload);
}
