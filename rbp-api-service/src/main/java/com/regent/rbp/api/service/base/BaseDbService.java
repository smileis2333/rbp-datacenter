package com.regent.rbp.api.service.base;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.base.BaseGoodsPriceDto;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenchungui
 * @date 2021/12/8
 * @description
 */
public interface BaseDbService {
    /**
     * 保存/更新自定义字段
     *
     * @param moduleId
     * @param tableNamePrefix
     * @param id
     * @param customFieldMap
     */
    boolean saveOrUpdateCustomFieldData(String moduleId, String tableNamePrefix, Long id, Map<String, Object> customFieldMap);

    default boolean saveOrUpdateCustomFieldData(String moduleId, String tableNamePrefix, Long id, List<CustomizeDataDto> customizeDataDtos) {
        Map<String, Object> customizeData;
        if (CollUtil.isNotEmpty(customizeDataDtos)) {
            Map<String, Object> customFieldMap = new HashMap<>();
            customizeDataDtos.forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            customizeData = customFieldMap;
            return saveOrUpdateCustomFieldData(moduleId, tableNamePrefix, id, customizeData);
        }
        return false;
    }

    /**
     * 批量保存/更新自定义字段
     *
     * @param moduleId
     * @param tableNamePrefix
     * @param customFieldMapList
     */
    boolean batchSaveOrUpdateCustomFieldData(String moduleId, String tableNamePrefix, List<Map<String, Object>> customFieldMapList);

    /**
     * 自定义字段查询
     *
     * @param tableNamePrefix
     * @param ids
     * @return
     */
    Map<Long, List<CustomizeDataDto>> getCustomizeColumnMap(String tableNamePrefix, List<Long> ids);

    /**
     * 获取模块已启用的自定义字段
     *
     * @param moduleIdList
     * @return
     */
    Map<String, List<CustomizeColumnDto>> getModuleCustomizeColumnListMap(List<String> moduleIdList);


    /**
     * 过滤填充自定义字段值
     *
     * @param moduleColumnDtoList
     * @param customizeDataList
     * @return
     */
    List<CustomizeDataDto> getAfterFillCustomizeDataList(List<CustomizeColumnDto> moduleColumnDtoList, List<CustomizeDataDto> customizeDataList);


    /**
     * 保存自定义字段
     *
     * @param tableNamePrefix
     * @param id
     * @param customFieldMap
     */
    boolean saveCustomFieldData(String tableNamePrefix, Long id, Map<String, Object> customFieldMap);

    /**
     * 查询自定义字段的值
     *
     * @param tableNamePrefix 表名前缀
     * @param id
     * @return
     */
    Map<String, Object> queryCustomData(String tableNamePrefix, Long id);

    /**
     * 获取货品基础价格、折扣吊牌价
     *
     * @param goodsIds
     * @return
     */
    List<BaseGoodsPriceDto> getBaseGoodsPriceByGoodsIds(List<Long> goodsIds);

    /**
     * 获取货品基础价格、折扣吊牌价
     *
     * @param goodsIds
     * @return
     */
    Map<Long, BaseGoodsPriceDto> getBaseGoodsPriceMapByGoodsIds(List<Long> goodsIds);

    String getDictionaryNewCode(String tableName);
}
