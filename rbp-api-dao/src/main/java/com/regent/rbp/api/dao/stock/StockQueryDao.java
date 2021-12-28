package com.regent.rbp.api.dao.stock;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.stock.StockDetail;
import com.regent.rbp.api.dto.stock.StockDataDto;
import com.regent.rbp.api.dto.stock.StockQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
public interface StockQueryDao extends BaseMapper<StockDetail> {

    IPage<StockDetail> searchPageData(@Param("pageModel") Page<StockDetail> pageModel, @Param("tableName") String tableName,
                                      @Param("queryParam") StockQueryParam queryParam);

    IPage<StockDataDto> searchStockList(@Param("pageModel") Page<StockDataDto> pageModel, @Param("tableName") String tableName,
                                        @Param("goodsIds") List<Long> goodsIds, @Param("barcodeList") List<String> barcodeList, @Param("channelIds") List<Long> channelIds);

}
