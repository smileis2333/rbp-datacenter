package com.regent.rbp.task.inno.controller;

import com.regent.rbp.task.inno.model.param.IntegralQueryParam;
import com.regent.rbp.task.inno.model.param.VipAddIntegralParam;
import com.regent.rbp.task.inno.service.VipIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description 积分模块
 * @Author czd
 * @Date 2021/10/20 13:17
 */
@RestController
@RequestMapping("api")
@Api(tags = "积分")
public class VipIntegralController {
    @Autowired
    private VipIntegralService vipIntegralService;

    @GetMapping("/Integral/{VIP}")
    @ApiOperation(value = "获取积分信息")
    public Map<String, Object> integral(@PathVariable(name = "VIP") String vip) {
        return vipIntegralService.get(vip);
    }

    @PostMapping("/VIPAddIntegral")
    @ApiOperation(value = "会员积分修改（增加/扣减）")
    public Map<String, String> vipAddIntegral(@RequestBody VipAddIntegralParam vipAddIntegralParam) {
        return vipIntegralService.vipAddIntegral(vipAddIntegralParam);
    }

    @PostMapping("/query")
    @ApiOperation(value = "会员积分流水明细（分页）")
    public Map<String, Object> query(@RequestBody IntegralQueryParam param) {
        return vipIntegralService.query(param);
    }
}
