package com.regent.rbp.task.inno.controller;

import com.regent.rbp.infrastructure.annotation.PassToken;
import com.regent.rbp.task.inno.service.RetailOrderInnoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 全渠道订单
 * @author: HaiFeng
 * @create: 2022/4/8 9:33
 */
@RestController
@RequestMapping("api")
@Api(tags = "全渠道订单")
public class RetailOrderInnoController {

    @Autowired
    private RetailOrderInnoService retailOrderInnoService;

    @PassToken
    @GetMapping("/GetOrderStatus")
    public Map<String, String> getOrderStatus(@RequestParam(name = "eorderid") String eorderid, @RequestParam(name = "barcode", required = false) String barcode) {
        Map<String, String> response = new HashMap<>();
        try {
            response = retailOrderInnoService.getOrderStatus(eorderid, barcode);
        }catch (Exception ex) {
            response.put("Flag", "-1");
            response.put("Message", "单据异常：" + ex.getMessage());
        }
        return response;
    }

}
