package com.regent.rbp.task.inno.controller;

import com.regent.rbp.task.inno.model.dto.StockDto;
import com.regent.rbp.task.inno.model.resp.StockRespDto;
import com.regent.rbp.task.inno.service.StockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program: rbp-datacenter
 * @description: 库存 Controller
 * @author: HaiFeng
 * @create: 2021-10-21 09:58
 */
@RestController
@RequestMapping("api")
@Api(tags = "库存")
public class StockController {

    @Autowired
    StockService stockService;

    @PostMapping("/stockQuery")
    public StockRespDto stockQuery(StockDto stockDto) {
        return stockService.stockQuery(stockDto);
    }
}
