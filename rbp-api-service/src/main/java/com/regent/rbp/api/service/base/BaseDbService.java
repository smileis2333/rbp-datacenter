package com.regent.rbp.api.service.base;

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
     * @param tableNamePrefix
     * @param id
     * @param customFieldMap
     */
    boolean saveOrUpdateCustomFieldData(String tableNamePrefix, Long id, Map<String, Object> customFieldMap);

    /**
     * 批量保存/更新自定义字段
     *
     * @param tableNamePrefix
     * @param customFieldMapList
     */
    boolean batchSaveOrUpdateCustomFieldData(String tableNamePrefix, List<Map<String, Object>> customFieldMapList);

    /**
     * 自定义字段查询
     *
     * @param tableNamePrefix
     * @param ids
     * @return
     */
    Map<Long, List<CustomizeDataDto>> getCustomizeColumnMap(String tableNamePrefix, List<Long> ids);

}
