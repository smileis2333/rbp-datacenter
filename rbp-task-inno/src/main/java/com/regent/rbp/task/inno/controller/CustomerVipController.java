package com.regent.rbp.task.inno.controller;

import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.service.CustomerVipService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Controller
 * @author: xuxing
 * @create: 2021-09-27 20:19
 */
@RestController
@RequestMapping("api")
@Api(tags = "会员档案")
public class CustomerVipController {

    private CustomerVipService customerVipService;

    @PostMapping("/AddCustomerVIP")
    public Map<String, String> AddCustomerVIP(CustomerVipDto customerVipDto) {
        HashMap<String, String> response = new HashMap<>();
        response.put("Flag", "1");
        response.put("Message", "");
        response.put("data", "");

        if(StringUtil.isEmpty(customerVipDto.getMobileTel())) {
            response.put("Flag", "1");
            response.put("Message", "会员手机号为空,已跳过");
            response.put("data", customerVipDto.getVIP());
            return response;
        }
        try {
            customerVipService.create(customerVipDto);

            response.put("Flag", "1");
            response.put("Message", "会员新增成功");
        }catch (Exception ex) {
            response.put("Flag", "-1");
            response.put("Message", "新增会员异常：" + ex.getMessage());
        }
        return response;
    }
}
