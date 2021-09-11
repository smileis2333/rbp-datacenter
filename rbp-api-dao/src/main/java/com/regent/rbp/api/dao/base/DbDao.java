package com.regent.rbp.api.dao.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
public interface DbDao {
    List<Map> selectTableDataByMap(@Param("tableName") String tableName, @Param("conditionMap")Map conditionMap);
}
