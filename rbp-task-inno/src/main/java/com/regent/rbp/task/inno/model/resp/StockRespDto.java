package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.api.dto.stock.StockDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 库存返回
 * @author: HaiFeng
 * @create: 2021-10-21 10:16
 */
@Data
public class StockRespDto {

    @ApiModelProperty(notes = "返回代码")
    private Integer code;

    @ApiModelProperty(notes = "返回信息")
    private String message;

    @ApiModelProperty(notes = "总的记录数")
    private Integer totalCount;

    @ApiModelProperty(notes = "库存数据")
    private List<StockDataDto> stockData;

    public StockRespDto(Integer code, String message, Integer totalCount, List<StockDataDto> stockData) {
        this.code = code;
        this.message = message;
        this.totalCount = totalCount;
        this.stockData = stockData;
    }

    public static StockRespDto success(Integer totalCount, List<StockDataDto> stockData) {
        return new StockRespDto(1, "", totalCount, stockData);
    }

    public static StockRespDto errorParameter(String message) {
        return new StockRespDto(-1, message, 0, null);
    }

}
