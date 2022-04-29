package com.regent.rbp.api.dao.stock;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.regent.rbp.api.core.stock.StockAdjustBill;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryParam;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryResult;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
public interface StockAdjustBillDao extends BaseMapper<StockAdjustBill> {
    IPage<StockAdjustBillQueryResult> searchPageData(IPage<?> page, @Param("query") StockAdjustBillQueryParam param);
}
