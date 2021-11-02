package com.regent.rbp.task.inno.controller;

import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.service.CustomerVipService;
import com.regent.rbp.task.inno.service.MemberService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private MemberService memberService;
    @Autowired
    MemberCardService memberCardService;

    @PostMapping("/AddCustomerVIP")
    public Map<String, String> AddCustomerVIP(@RequestBody CustomerVipDto customerVipDto) {
        Map<String, String> response = new HashMap<>();
        try {
            response = memberService.save(customerVipDto, true);
        }catch (Exception ex) {
            response.put("Flag", "-1");
            response.put("Message", "新增会员异常：" + ex.getMessage());
        }
        return response;
    }

    @PostMapping("/UpdateCustomerVIP")
    public Map<String, String> UpdateCustomerVIP(@RequestBody CustomerVipDto customerVipDto) {
        Map<String, String> response = new HashMap<>();
        try {
            response = memberService.save(customerVipDto, false);
        }catch (Exception ex) {
            response.put("Flag", "-1");
            response.put("Message", "更新会员异常：" + ex.getMessage());
        }
        return response;
    }
}
