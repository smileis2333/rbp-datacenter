package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.goods.GoodsTagPrice;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.CustomizeColumnDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.goods.GoodsTagPriceDao;
import com.regent.rbp.api.dto.base.BaseGoodsPriceDto;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeColumnValueDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.CustomizeColumnTypeEnum;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/12/8
 * @description
 */
@Log4j2
@Service
public class BaseDbServiceBean implements BaseDbService {

    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private CustomizeColumnDao customizeColumnDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsTagPriceDao goodsTagPriceDao;

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
    public boolean saveOrUpdateCustomFieldData(String moduleId, String tableNamePrefix, Long id, Map<String, Object> customFieldMap) {
        if (CollUtil.isEmpty(customFieldMap)) {
            return false;
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            log.error(LanguageUtil.getMessageByParams("dataNotExist", new Object[]{tableName}));
            return false;
        }
        // 验证模块是否启用当前自定义字段
        Map<String, List<CustomizeColumnDto>> listMap = this.getModuleCustomizeColumnListMap(Arrays.asList(moduleId));
        if (CollUtil.isEmpty(listMap)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{LanguageUtil.getMessage("customizeColumn")});
        }
        Map<String, CustomizeColumnDto> columnDtoMap = listMap.get(moduleId).stream().collect(Collectors.toMap(CustomizeColumnDto::getCode, Function.identity(), (x1, x2) -> x1));
        this.validateCustomFiledMap(customFieldMap, columnDtoMap);
        // 新增
        StringBuilder insertSqlPrefix = new StringBuilder("Insert into " + tableName + " ( id, ");
        StringBuilder insertValue = new StringBuilder("values (" + id + ",");
        for (Map.Entry<String, Object> entry : customFieldMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (null != value && !StrUtil.EMPTY.equals(value) && !"id".equals(key)) {
                CustomizeColumnDto columnDto = columnDtoMap.get(key);
                // 单选类型，需要通过值获取ID
                if (CustomizeColumnTypeEnum.SELECT.equals(CustomizeColumnTypeEnum.getByPageType(columnDto.getType()))) {
                    Object finalValue = value;
                    value = columnDto.getColumnValueList().stream().filter(f -> finalValue.equals(f.getValue())).findFirst().get().getId();
                }
                //value不能null,也不能为 ""
                insertSqlPrefix.append(key).append(",");
                insertValue.append(" '").append(value).append("',");
            }
        }
        int index = insertSqlPrefix.lastIndexOf(",");
        insertSqlPrefix.replace(index, index + 1, " ) ");
        index = insertValue.lastIndexOf(",");
        insertValue.replace(index, index + 1, " ) ");
        String insertSql = insertSqlPrefix.append(insertValue).toString();
        // 删除
        baseDbDao.deleteSql(String.format("delete from %s where id=%s", tableName, id));
        //新增
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
    public boolean batchSaveOrUpdateCustomFieldData(String moduleId, String tableNamePrefix, List<Map<String, Object>> customFieldMapList) {
        if (CollUtil.isEmpty(customFieldMapList)) {
            return false;
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            log.error(LanguageUtil.getMessageByParams("dataNotExist", new Object[]{tableName}));
            return false;
        }
        // 校验字段是否存在
        Set<String> fields = new HashSet<>();
        // 字段名
        List<Object> ids = new ArrayList<>();
        customFieldMapList.forEach(item -> {
            fields.addAll(item.keySet());
            if (!item.keySet().contains("id")) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{"customFieldMap.id"});
            }
            ids.add(item.get("id"));
        });
        // 最后一个字段名
        String lastKey = null;
        for (String key : fields) {
            //数据库存在字段 做处理，不存在直接忽略
            if (!"id".equals(key) && baseDbDao.isExistField(tableName, key) == 0) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{tableName + StrUtil.DOT + key});
            }
            lastKey = key;
        }
        // 验证模块是否启用当前自定义字段
        Map<String, List<CustomizeColumnDto>> listMap = this.getModuleCustomizeColumnListMap(Arrays.asList(moduleId));
        if (CollUtil.isEmpty(listMap)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{LanguageUtil.getMessage("customizeColumn")});
        }
        Map<String, CustomizeColumnDto> columnDtoMap = listMap.get(moduleId).stream().collect(Collectors.toMap(CustomizeColumnDto::getCode, Function.identity(), (x1, x2) -> x1));
        // 新增
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO " + tableName);
        sb.append(String.format(" (%s) values ", String.join(StrUtil.COMMA, fields)));
        String finalLastKey = lastKey;
        customFieldMapList.forEach(map -> {
            this.validateCustomFiledMap(map, columnDtoMap);
            sb.append(" (");
            for (String key : fields) {
                Object value = Optional.ofNullable(map.get(key)).orElse(null);
                // 单选类型，需要通过值获取ID
                CustomizeColumnDto columnDto = columnDtoMap.get(key);
                if (null != columnDto && CustomizeColumnTypeEnum.SELECT.equals(CustomizeColumnTypeEnum.getByPageType(columnDto.getType()))) {
                    Object finalValue = value;
                    value = columnDto.getColumnValueList().stream().filter(f -> finalValue.equals(f.getValue())).findFirst().get().getId();
                }
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
            log.error(LanguageUtil.getMessageByParams("dataNotExist", new Object[]{tableName}));
            return new HashMap<>();
        }
        List<CustomizeDataDto> list = new ArrayList<>();
        List<Map<Object, Object>> mapList = baseDbDao.getListMap(String.format("select * from %s where id in %s", tableName, AppendSqlUtil.getInSql(ids.stream().collect(Collectors.toList()))));
        if (CollUtil.isEmpty(mapList)) {
            return new HashMap<>();
        }
        mapList.forEach(item -> {
            Long id = (Long) item.get("id");
            item.forEach((key, value) -> {
                if (!"id".equals(key)) {
                    CustomizeDataDto columnDto = new CustomizeDataDto();
                    list.add(columnDto);

                    columnDto.setId(id);
                    columnDto.setCode(key.toString());
                    columnDto.setValue(Optional.ofNullable(value).orElse(StrUtil.EMPTY).toString());
                }
            });
        });
        // 根据id分组
        return list.stream().collect(Collectors.groupingBy(CustomizeDataDto::getId));
    }

    /**
     * 获取模块已启用自定义字段
     *
     * @param moduleIdList
     * @return
     */
    @Override
    public Map<String, List<CustomizeColumnDto>> getModuleCustomizeColumnListMap(List<String> moduleIdList) {
        if (CollUtil.isEmpty(moduleIdList)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotNull", new Object[]{"moduleIdList"});
        }
        List<CustomizeColumnDto> columnDtoList = customizeColumnDao.selectCustomizeColumnList(moduleIdList);
        if (CollUtil.isEmpty(columnDtoList)) {
            return new HashMap<>();
        }
        List<CustomizeColumnValueDto> columnValueDtoList = customizeColumnDao.selectCustomizeColumnValueList(StreamUtil.toList(columnDtoList, CustomizeColumnDto::getId));
        Map<Long, List<CustomizeColumnValueDto>> valueGroupMap = new HashMap<>();
        if (CollUtil.isNotEmpty(columnValueDtoList)) {
            valueGroupMap = columnValueDtoList.stream().collect(Collectors.groupingBy(CustomizeColumnValueDto::getCustomizeColumnId));
        }
        // 填充自定义字段选项值
        for (CustomizeColumnDto columnDto : columnDtoList) {
            columnDto.setColumnValueList(valueGroupMap.get(columnDto.getId()));
        }
        return columnDtoList.stream().collect(Collectors.groupingBy(CustomizeColumnDto::getModuleId));
    }

    /**
     * 过滤填充自定义字段值
     *
     * @param moduleColumnDtoList
     * @param customizeDataList
     * @return
     */
    @Override
    public List<CustomizeDataDto> getAfterFillCustomizeDataList(List<CustomizeColumnDto> moduleColumnDtoList, List<CustomizeDataDto> customizeDataList) {
        List<CustomizeDataDto> newCustomizeDataList = new ArrayList<>();
        if (CollUtil.isEmpty(moduleColumnDtoList) || CollUtil.isEmpty(customizeDataList)) {
            return newCustomizeDataList;
        }
        Map<String, CustomizeColumnDto> columnDtoMap = moduleColumnDtoList.stream().collect(Collectors.toMap(CustomizeColumnDto::getCode, Function.identity(), (x1, x2) -> x1));
        customizeDataList.stream().filter(f -> columnDtoMap.keySet().contains(f.getCode())).forEach(item -> {
            CustomizeColumnDto columnDto = columnDtoMap.get(item.getCode());
            if (CollUtil.isNotEmpty(columnDto.getColumnValueList()) && CustomizeColumnTypeEnum.SELECT.equals(CustomizeColumnTypeEnum.getByPageType(columnDto.getType()))) {
                Optional<CustomizeColumnValueDto> optional = columnDto.getColumnValueList().stream().filter(f -> item.getValue().equals(f.getId().toString())).findFirst();
                if (optional.isPresent()) {
                    item.setValue(optional.get().getValue());
                }
            }
            newCustomizeDataList.add(item);
        });

        return newCustomizeDataList;
    }

    /**
     * 自定义字段格式验证
     *
     * @param map
     * @param columnDtoMap
     */
    private void validateCustomFiledMap(Map<String, Object> map, Map<String, CustomizeColumnDto> columnDtoMap) {
        map.forEach((key, value) -> {
            if (null != value && !StrUtil.EMPTY.equals(value) && !"id".equals(key)) {
                CustomizeColumnDto columnDto = columnDtoMap.get(key);
                if (null == columnDto) {
                    throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExistOrDisabled", new Object[]{key});
                }
                // 判断字段类型参数
                switch (CustomizeColumnTypeEnum.getByPageType(columnDto.getType())) {
                    case DATE: {
                        try {
                            DateUtil.getDate(value.toString(), DateUtil.SHORT_DATE_FORMAT);
                        } catch (Exception e) {
                            throw new BusinessException(ResponseCode.PARAMS_ERROR, "customizeColumnValueError", new Object[]{key, LanguageUtil.getMessage("dateType")});
                        }
                        break;
                    }
                    case SELECT: {
                        List<CustomizeColumnValueDto> valueDtoList = columnDto.getColumnValueList();
                        if (CollUtil.isEmpty(valueDtoList) || !StreamUtil.toSet(valueDtoList, CustomizeColumnValueDto::getValue).contains(value)) {
                            throw new BusinessException(ResponseCode.PARAMS_ERROR, "customizeColumnValueError", new Object[]{key, LanguageUtil.getMessage("selectOption")});
                        }
                        break;
                    }
                    case NUMBER: {
                        if (!StringUtils.isNumeric(value.toString())) {
                            throw new BusinessException(ResponseCode.PARAMS_ERROR, "customizeColumnValueError", new Object[]{key, LanguageUtil.getMessage("numberType")});
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 保存自定义数据
     *
     * @param tableNamePrefix 表名前缀   系统规定自定义字段用 "_custom"做结尾，此处给表名前缀。
     * @param id              数据id
     * @param customFieldMap  自定义字段集合
     */
    @Override
    public boolean saveCustomFieldData(String tableNamePrefix, Long id, Map<String, Object> customFieldMap) {
        if (CollUtil.isEmpty(customFieldMap)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "notNull");
        }
        String tableName = tableNamePrefix + "_custom";
        if (baseDbDao.isExistTable(tableName) == 0) {
            log.error(LanguageUtil.getMessageByParams("dataNotExist", new Object[]{tableName}));
            return false;
        }
        StringBuilder insertSqlPrefix = new StringBuilder("Insert into " + tableName + " ( id, ");
        StringBuilder insertValue = new StringBuilder("values (" + id + ",");
        for (Map.Entry<String, Object> entry : customFieldMap.entrySet()) {
            String key = entry.getKey();
            //数据库存在字段 做处理，不存在直接忽略
            if (baseDbDao.isExistField(tableName, key) == 0) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{LanguageUtil.getMessage("customizeColumn")});
            }
            //value不能null,也不能为 ""
            if (null != entry.getValue() && !StrUtil.EMPTY.equals(entry.getValue()) && !InformationConstants.StringConstants.ID.equals(key)) {
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


    @Override
    public Map<String, Object> queryCustomData(String tableNamePrefix, Long id) {
        String tableName = tableNamePrefix + "_custom";
        Integer existTableInt = baseDbDao.isExistTable(tableName);
        Map<String, Object> resultMap = new HashMap<>();
        if (existTableInt == 0) {
            log.error(LanguageUtil.getMessageByParams("dataNotExist", new Object[]{tableName}));
            return resultMap;
        }
        resultMap = baseDbDao.queryCustomDataById(tableName, id);
        if (CollUtil.isNotEmpty(resultMap)) {
            Iterator<Map.Entry<String, Object>> iteratorMap = resultMap.entrySet().iterator();
            while (iteratorMap.hasNext()) {
                Map.Entry<String, Object> entry = iteratorMap.next();
                if (entry.getValue() instanceof Date) {
                    resultMap.put(entry.getKey(), DateUtil.getDateStr((Date) entry.getValue(), DateUtil.FULL_DATE_FORMAT));
                }
                if ("id".equalsIgnoreCase(entry.getKey())) {
                    iteratorMap.remove();
                }
            }
        }
        return resultMap;
    }

    /**
     * 获取货品基础价格
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<BaseGoodsPriceDto> getBaseGoodsPriceByGoodsIds(List<Long> goodsIds) {
        List<BaseGoodsPriceDto> resultList = new ArrayList<>();
        if (CollUtil.isEmpty(goodsIds)) {
            return resultList;
        }
        List<Goods> goodsList = goodsDao.selectBatchIds(goodsIds);
        // 吊牌价
        List<GoodsTagPrice> tagPriceList = goodsTagPriceDao.selectList(new QueryWrapper<GoodsTagPrice>().select("goods_id,tag_price,price_type_id").in("goods_id", goodsIds)
                .orderByDesc("goods_id,price_type_id").groupBy("goods_id"));
        Map<Long, BigDecimal> goodsTagPriceMap = CollUtil.isEmpty(tagPriceList) ? new HashMap<>() : tagPriceList.stream().collect(Collectors.toMap(GoodsTagPrice::getGoodsId, GoodsTagPrice::getTagPrice, (x1, x2) -> x1));
        for (Goods goods : goodsList) {
            BaseGoodsPriceDto discountDto = new BaseGoodsPriceDto(goods.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            // 采购价
            if (null != goods.getPlanCostPrice()) {
                discountDto.setBalancePrice(goods.getPlanCostPrice());
            }
            // 默认0
            BigDecimal tagPrice = goodsTagPriceMap.get(goods.getId());
            if (null != tagPrice) {
                discountDto.setTagPrice(tagPrice);
            }
            // 采购价为0，默认取吊牌价
            if (discountDto.getBalancePrice().equals(BigDecimal.ZERO)) {
                discountDto.setBalancePrice(discountDto.getTagPrice());
            }
            // 折扣
            if (discountDto.getBalancePrice().compareTo(BigDecimal.ZERO) > 0 && discountDto.getTagPrice().compareTo(BigDecimal.ZERO) > 0) {
                discountDto.setDiscount(discountDto.getBalancePrice().divide(discountDto.getTagPrice(), 4, BigDecimal.ROUND_HALF_UP));
            }
            resultList.add(discountDto);
        }

        return resultList;
    }

    /**
     * 获取货品基础价格
     *
     * @param goodsIds
     * @return
     */
    @Override
    public Map<Long, BaseGoodsPriceDto> getBaseGoodsPriceMapByGoodsIds(List<Long> goodsIds) {
        List<BaseGoodsPriceDto> list = getBaseGoodsPriceByGoodsIds(goodsIds);
        return CollUtil.isEmpty(list) ? new HashMap<>() : list.stream().collect(Collectors.toMap(BaseGoodsPriceDto::getGoodsId, Function.identity(), (x1, x2) -> x1));
    }

    @Override
    public String getDictionaryNewCode(String tableName) {
        String sql = String.format("        select\n" +
                "            n+1 new_code\n" +
                "        from\n" +
                "            (\n" +
                "                select\n" +
                "                    cast(code as signed) n\n" +
                "                from\n" +
                "                    %s \n" +
                "                where\n" +
                "                    code regexp  \"^[0-9]+$\"\n" +
                "                order by\n" +
                "                    n desc) ns limit 1", tableName);
        String newCode = baseDbDao.getStringDataBySql(sql);
        return StrUtil.isBlank(newCode) ? "1" : newCode;
    }

}
