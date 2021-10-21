package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.dto.StockDto;
import com.regent.rbp.task.inno.model.resp.StockRespDto;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 库存 Service
 * @author: HaiFeng
 * @create: 2021-10-21 10:08
 */
public interface StockService {

    /**
     * 库存查询
     * @param dto
     * @return
     */
    StockRespDto stockQuery(StockDto dto);
}
