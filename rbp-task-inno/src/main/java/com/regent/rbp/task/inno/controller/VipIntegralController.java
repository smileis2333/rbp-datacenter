package com.regent.rbp.task.inno.controller;

import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.model.param.IntegralQueryParam;
import com.regent.rbp.task.inno.model.param.VipAddIntegralParam;
import com.regent.rbp.task.inno.service.VipIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/Integral")
    @ApiOperation(value = "获取积分信息")
    public Map<String, Object> integral(@RequestParam(name = "VIP") String vip) {
        return vipIntegralService.get(vip);
    }

    @PostMapping("/VIPAddIntegral")
    @ApiOperation(value = "会员积分修改（增加/扣减）")
    public Map<String, String> vipAddIntegral(@RequestBody VipAddIntegralParam vipAddIntegralParam) {
        return vipIntegralService.vipAddIntegral(vipAddIntegralParam);
    }

    @GetMapping("/VipIntegralDetali")
    @ApiOperation(value = "会员积分流水明细（分页）")
    public Map<String, Object> query(@RequestParam (name = "vip",required = false) String vip,
                                     @RequestParam (name = "pageNo") int pageNo,
                                     @RequestParam (name = "pageSize") int pageSize,
                                     @RequestParam (name = "startDate",required = false) String startDate,
                                     @RequestParam (name = "endDate",required = false) String endDate,
                                     @RequestParam (name = "sort",required = false) String sort) {
        IntegralQueryParam param = new IntegralQueryParam();
        param.setVip(vip);
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        if (StringUtils.isNotEmpty(startDate)) {
            param.setStartDate(DateUtil.getDate(startDate, "YYYY-MM-dd"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            param.setEndDate(DateUtil.getDate(endDate, "YYYY-MM-dd"));
        }
        param.setSort(sort);
        return vipIntegralService.query(param);
    }
}
