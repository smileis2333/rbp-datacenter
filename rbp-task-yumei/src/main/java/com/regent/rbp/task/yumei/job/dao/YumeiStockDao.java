package com.regent.rbp.task.yumei.job.dao;

import com.regent.rbp.common.dao.BaseDao;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/08
 * @description
 */
@Mapper
public interface YumeiStockDao extends BaseDao<UsableStockDetail> {
    int overwriteUsableStockDetailList(@Param("usableStockDetailList") List<UsableStockDetail> usableStockDetailList);
}
