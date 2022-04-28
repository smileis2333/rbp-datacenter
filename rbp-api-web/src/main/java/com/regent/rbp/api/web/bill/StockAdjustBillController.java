package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryParam;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryResult;
import com.regent.rbp.api.dto.stock.StockAdjustBillSaveParam;
import com.regent.rbp.api.service.stock.StockAdjustBillService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_STOCK_ADJUST)
@Api(tags = "库存调整单")
public class StockAdjustBillController {

    @Autowired
    private StockAdjustBillService stockAdjustBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<StockAdjustBillQueryResult> query(@RequestBody StockAdjustBillQueryParam param) {
        PageDataResponse<StockAdjustBillQueryResult> result = stockAdjustBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid StockAdjustBillSaveParam param) {
        DataResponse result = stockAdjustBillService.save(param);
        return result;
    }
}
