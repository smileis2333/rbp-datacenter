package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/12/8
 * @description
 */
@Service
public class BaseDbServiceBean implements BaseDbService {

    @Autowired
    private BaseDbDao baseDbDao;

    /**
     * 新增/更新自定义字段
     *
     * @param tableNamePrefix
     * @param id
     * @param customFieldMap
     * @return
     */
    @Transactional
    @Override
    public boolean saveOrUpdateCustomFieldData(String tableNamePrefix, Long id, Map<String, Object> customFieldMap) {
        if (CollUtil.isEmpty(customFieldMap)) {
            return false;
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{tableName});
        }
        // 删除
        baseDbDao.deleteSql(String.format("delete from %s where id=%s", tableName, id));
        // 新增
        StringBuilder insertSqlPrefix = new StringBuilder("Insert into " + tableName + " ( id, ");
        StringBuilder insertValue = new StringBuilder("values (" + id + ",");
        for (Map.Entry<String, Object> entry : customFieldMap.entrySet()) {
            String key = entry.getKey();
            //数据库存在字段 做处理，不存在直接忽略
            if (baseDbDao.isExistField(tableName, key) == 0) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{tableName + StrUtil.DOT + key});
            }
            //value不能null,也不能为 ""
            if (null != entry.getValue() && !StrUtil.EMPTY.equals(entry.getValue()) && !"id".equals(key)) {
                insertSqlPrefix.append(key).append(",");
                insertValue.append(" '").append(entry.getValue()).append("',");
            }
        }
        int index = insertSqlPrefix.lastIndexOf(",");
        insertSqlPrefix.replace(index, index + 1, " ) ");
        index = insertValue.lastIndexOf(",");
        insertValue.replace(index, index + 1, " ) ");
        String insertSql = insertSqlPrefix.append(insertValue).toString();
        int count = baseDbDao.insertSql(insertSql);
        return count == 1;
    }

    /**
     * 批量新增/更新自定义字段
     *
     * @param tableNamePrefix
     * @param customFieldMapList
     * @return
     */
    @Transactional
    @Override
    public boolean batchSaveOrUpdateCustomFieldData(String tableNamePrefix, List<Map<String, Object>> customFieldMapList) {
        if (CollUtil.isEmpty(customFieldMapList)) {
            return false;
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{tableName});
        }
        // 字段名
        Set<String> fields = new HashSet<>();
        List<Object> ids = new ArrayList<>();
        customFieldMapList.forEach(param -> {
            fields.addAll(param.keySet());
            if (!param.keySet().contains("id")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{"customFieldMap.id"});
            }
            ids.add(param.get("id"));
        });
        // 最后一个字段名
        Iterator fieldIterator = fields.iterator();
        String lastKey = null;
        while (fieldIterator.hasNext()) {
            lastKey = fieldIterator.next().toString();
        }
        // 新增
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO " + tableName);
        sb.append(String.format(" (%s) values ", String.join(StrUtil.COMMA, fields)));
        String finalLastKey = lastKey;
        customFieldMapList.forEach(map -> {
            sb.append(" (");
            for (String key : fields) {
                Object value = Optional.ofNullable(map.get(key)).orElse(null);
                sb.append("'").append(value).append("'");
                if (!finalLastKey.equals(key)) {
                    sb.append(",");
                }
            }
            sb.append(" ),");
        });
        // 删除
        baseDbDao.deleteSql(String.format("delete from %s where id in %s", tableName, AppendSqlUtil.getInSql(ids)));
        // 移除最后一个逗号
        String insertSql = sb.toString();
        int count = baseDbDao.insertSql(insertSql.substring(0, insertSql.length() - 1));
        return count == 1;
    }

    /**
     * 自定义字段查询
     *
     * @param tableNamePrefix
     * @param ids
     * @return
     */
    @Override
    public Map<Long, List<CustomizeDataDto>> getCustomizeColumnMap(String tableNamePrefix, List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "notNull");
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{tableName});
        }
        List<CustomizeDataDto> list = new ArrayList<>();
        List<Map<Object, Object>> mapList = baseDbDao.getListMap(String.format("select * from %s where id in %s", tableName, AppendSqlUtil.getInSql(ids.stream().collect(Collectors.toList()))));
        if (CollUtil.isEmpty(mapList)) {
            return null;
        }
        mapList.forEach(item -> {
            item.forEach((key, value) -> {
                CustomizeDataDto columnDto = new CustomizeDataDto();
                list.add(columnDto);
                if ("id".equals(key)) {
                    columnDto.setId((Long) value);
                } else {
                    columnDto.setCode(key.toString());
                    columnDto.setValue(Optional.ofNullable(value).orElse(StrUtil.EMPTY).toString());
                }
            });
        });
        // 根据id分组
        return list.stream().collect(Collectors.groupingBy(CustomizeDataDto::getId));
    }
}
