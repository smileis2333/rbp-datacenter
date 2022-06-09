package com.regent.rbp.task.yumei.controller;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.common.model.basic.dto.ModuleConsumptionDto;
import com.regent.rbp.task.yumei.model.AuditData;
import com.regent.rbp.task.yumei.service.OffshopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@RestController
@RequestMapping("api/yumei/offshop")
@Api(tags = "门店类")
public class YumeiOffshopBillController {
    @Autowired
    private OffshopService offshopService;

    @ApiOperation(value = "审核")
    @PostMapping("return/check")
    public DataResponse check(@RequestBody ModuleConsumptionDto consumptionDto) {
        String billNo = consumptionDto.getContentNo();
        offshopService.createReturnOrder(billNo, AuditData.builder().checkBy(consumptionDto.getCheckBy()).build());
        return ModelDataResponse.Success(billNo);
    }

    @ApiOperation(value = "反审核")
    @PostMapping("return/uncheck")
    public DataResponse unCheck(@RequestBody ModuleConsumptionDto consumptionDto) {
        String billNo = consumptionDto.getContentNo();
        offshopService.cancelReturnOrder(billNo);
        return ModelDataResponse.Success(billNo);
    }
}
