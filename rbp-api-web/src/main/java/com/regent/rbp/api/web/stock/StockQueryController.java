package com.regent.rbp.api.web.stock;

import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.stock.StockQueryParam;
import com.regent.rbp.api.dto.stock.StockQueryResult;
import com.regent.rbp.api.service.stock.StockQueryService;
import com.regent.rbp.api.web.constants.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description 库存查询
 */
@RestController(value = "datacenterStockQueryController")
@RequestMapping(ApiConstants.API_STOCK_QUERY)
public class StockQueryController {

    @Autowired
    private StockQueryService stockQueryService;

    @PostMapping("/query")
    public PageDataResponse<StockQueryResult> query(@RequestBody StockQueryParam param) {
        return stockQueryService.query(param);
    }
}
