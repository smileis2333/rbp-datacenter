package com.regent.rbp.api.dao.base;

import com.regent.rbp.api.core.base.ModuleBusinessType;
import com.regent.rbp.api.core.base.SizeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
public interface BaseDbDao {

    List<Map> selectTableDataByMap(@Param("tableName") String tableName, @Param("conditionMap") Map conditionMap);

    void insertMap(@Param("tableName") String tableName, @Param("map") Map<String, Object> map);

    void updateMapById(@Param("tableName") String tableName, @Param("map") Map<String, Object> map);

    Long getLongDataBySql(@Param("sql") String sql);

    List<Long> getLongListDataBySql(@Param("sql") String sql);

    Integer getIntegerDataBySql(@Param("sql") String sql);

    String getStringDataBySql(@Param("sql") String sql);

    Integer insertSql(@Param("sql") String sql);

    void deleteSql(@Param("sql") String sql);

    void updateSql(@Param("sql") String sql);

    Integer isExistField(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    Integer isExistTable(@Param("tableName") String tableName);

    /**
     * 获取第一个已启用模块业务类型
     *
     * @param baseModuleId       基础模块编码
     * @param baseBusinessTypeId 基础业务类型编码
     * @return
     */
    ModuleBusinessType getOneModuleBusinessType(@Param("baseModuleId") String baseModuleId, @Param("baseBusinessTypeId") Long baseBusinessTypeId);

    List<Map<Object, Object>> getListMap(@Param("sql") String sql);

    List<SizeDetail> getSizeNameList(@Param("goodsIdList") List<Long> goodsIdList, @Param("sizeNameList") List<String> sizeNameList);

    void dropTemplateTable(@Param("tableName") String tableName);
}
