package com.regent.rbp.task.inno.controller;

import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.service.RetailOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 全渠道订单
 * @author: HaiFeng
 * @create: 2022/4/6 13:21
 */
@RestController
@RequestMapping("api")
@Api(tags = "全渠道订单")
public class RetailOrderBillController {

    @Autowired
    private RetailOrderBillService retailOrderBillService;

    @GetMapping("/GetOrderStatus")
    public Map<String, String> getOrderStatus(@RequestParam(name = "eorderid") String eorderid, @RequestParam(name = "barcode") String barcode) {
        Map<String, String> response = new HashMap<>();
        try {
            response = retailOrderBillService.getOrderStatus(eorderid, barcode);
        }catch (Exception ex) {
            response.put("Flag", "-1");
            response.put("Message", "单据异常：" + ex.getMessage());
        }
        return response;
    }


}
