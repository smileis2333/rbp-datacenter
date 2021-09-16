package com.regent.rbp.api.dao.base;

import com.regent.rbp.api.core.base.ModuleBusinessType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
public interface DbDao {

    List<Map> selectTableDataByMap(@Param("tableName") String tableName, @Param("conditionMap")Map conditionMap);

    void insertMap(@Param("tableName") String tableName, @Param("map")Map<String, Object> map);

    void updateMapById(@Param("tableName") String tableName, @Param("map")Map<String, Object> map);

    Long getLongDataBySql(@Param("sql") String sql);

    Integer getIntegerDataBySql(@Param("sql") String sql);

    String getStringDataBySql(@Param("sql") String sql);

    /**
     * 获取第一个已启用模块业务类型
     *
     * @param baseModuleId       基础模块编码
     * @param baseBusinessTypeId 基础业务类型编码
     * @return
     */
    ModuleBusinessType getOneModuleBusinessType(@Param("baseModuleId") String baseModuleId, @Param("baseBusinessTypeId") Long baseBusinessTypeId);
}
