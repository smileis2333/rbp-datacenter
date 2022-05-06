package com.regent.rbp.task.yumei.controller;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.task.yumei.param.YumeiFinancialSettlementBillSaveParam;
import com.regent.rbp.task.yumei.service.FinancialSettlementBillService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@RestController
@RequestMapping("api/financialSettlementBill")
@Api(tags = "结算单")
public class FinancialSettlementBillController {

    @Autowired
    private FinancialSettlementBillService financialSettlementBillService;

    @PostMapping
    public ModelDataResponse<String> save(@Validated @RequestBody YumeiFinancialSettlementBillSaveParam param) {
        return financialSettlementBillService.save(param);
    }
}
