package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;

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


}
