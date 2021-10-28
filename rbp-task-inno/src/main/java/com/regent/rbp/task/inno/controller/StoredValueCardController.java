package com.regent.rbp.task.inno.controller;

import com.regent.rbp.api.dto.storedvaluecard.AddVipValueParam;
import com.regent.rbp.task.inno.service.StoredCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 9:58
 */
@RestController
@RequestMapping("api")
@Api(tags = "储值卡")
public class StoredValueCardController {
    @Autowired
    private StoredCardService storedCardService;

    @GetMapping("/VIPValue")
    @ApiOperation(value = "（单个）会员可用储值余额读取")
    public Map<String, Object> storedValueCard(@RequestParam(name = "VIP") String vip) {
        return storedCardService.get(vip);
    }

    @PostMapping("/AddVIPValue")
    @ApiOperation(value = "会员储值增加、扣减")
    public Map<String, String> addVipValue(@RequestBody AddVipValueParam vipValueParam) {
        return storedCardService.addVipValue(vipValueParam);
    }

    @GetMapping("/VIPValueList")
    @ApiOperation(value = "会员储值流水账读取")
    public Map<String, Object> query(@RequestParam(name = "VIP") String vip) {
        return storedCardService.query(vip);
    }
}
