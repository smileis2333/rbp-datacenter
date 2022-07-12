package com.regent.rbp.api.dao.base;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.base.ModuleBusinessType;
import com.regent.rbp.api.core.base.SizeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    default Boolean getBooleanDataBySql(String sql) {
        String str = getStringDataBySql(sql);
        return Boolean.valueOf(str);
    }

    List<String> getStringListDataBySql(@Param("sql") String sql);

    Integer insertSql(@Param("sql") String sql);

    Integer deleteSql(@Param("sql") String sql);

    Integer updateSql(@Param("sql") String sql);

    Integer isExistField(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    /**
     * 查询是否存在对应的column name对应的column
     * @param tableName
     * @param columnName
     * @return
     */
    Integer isExistColumnName(@Param("tableName") String tableName, @Param("columnName") String columnName);

    Integer isExistTable(@Param("tableName") String tableName);

    Boolean isExist(@Param("checkSql") String checkSql);

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

    /**
     *
     * @param goodsCode
     * @param sizeNames
     * @param goodsCodeIdMap
     * @return
     */
    @Deprecated
    default Map<Long, Map<String, Long>> getGoodsIdSizeNameIdMap(List<String> goodsCode, List<String> sizeNames, Map<String, Long> goodsCodeIdMap) {
        if (CollUtil.isEmpty(goodsCode) || CollUtil.isEmpty(sizeNames)) {
            throw new RuntimeException("illegal operation query format invalid");
        }
        if (goodsCode.size() != sizeNames.size()) {
            throw new RuntimeException("illegal operation, query size id relative goods id and sizeName must be align");
        }
        if (CollUtil.isEmpty(goodsCodeIdMap)) {
            throw new RuntimeException("must supply code to id about goods");
        }
        if (!goodsCodeIdMap.keySet().containsAll(goodsCode.stream().collect(Collectors.toSet()))) {
            throw new RuntimeException("illegal operation, please check the map about goodsCode to goodsId");
        }

        List<Long> goodsIds = goodsCode.stream().map(code -> goodsCodeIdMap.get(code)).collect(Collectors.toList());
        Map<Long, Map<String, Long>> gniMap = getSizeNameList(goodsIds, sizeNames).stream().collect(Collectors.groupingBy(SizeDetail::getGoodsId, Collectors.collectingAndThen(Collectors.toMap(SizeDetail::getName, SizeDetail::getId), Collections::unmodifiableMap)));
        return gniMap;
    }

    void dropTemplateTable(@Param("tableName") String tableName);

    /**
     * 根据id查询自定义数据
     *
     * @param tableName
     * @param id
     * @return
     */
    Map<String, Object> queryCustomDataById(@Param("tableName") String tableName, @Param("id") Long id);

}
