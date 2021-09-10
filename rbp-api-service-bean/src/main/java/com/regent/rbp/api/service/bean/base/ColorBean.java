package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.ColorGroup;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.ColorGroupDao;
import com.regent.rbp.api.dto.base.ColorData;
import com.regent.rbp.api.dto.base.ColorDeleteParam;
import com.regent.rbp.api.dto.base.ColorQueryParam;
import com.regent.rbp.api.dto.base.ColorSaveParam;
import com.regent.rbp.api.dto.base.ColorUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.ColorService;
import com.regent.rbp.api.service.base.context.ColorDeleteContext;
import com.regent.rbp.api.service.base.context.ColorQueryContext;
import com.regent.rbp.api.service.base.context.ColorSaveContext;
import com.regent.rbp.api.service.base.context.ColorUpdateContext;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.utils.FieldFilterTool;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description
 */
@Service
public class ColorBean extends ServiceImpl<ColorDao, Color> implements ColorService {

    @Autowired
    private ColorDao colorDao;

    @Autowired
    private ColorGroupDao colorGroupDao;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<ColorData> searchPageData(ColorQueryParam param) {
        ColorQueryContext context = new ColorQueryContext();
        this.convertQueryContext(param, context);
        PageDataResponse<ColorData> resultList = new PageDataResponse<>();
        // 验证查询字段存在
        if (StringUtil.isNotEmpty(context.getFields())) {
            if (!context.getFields().contains("colorCode") && !context.getFields().contains("colorName") && !context.getFields().contains("colorGroup")) {
                resultList.setCode(ResponseCode.PARAMS_ERROR);
                resultList.setMessage(LanguageUtil.getMessage("dataNotExist", new String[]{"fields: " + context.getFields()}));
                return resultList;
            }
        }
        // 组装查询条件
        QueryWrapper<Color> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,code,name,group_id");
        queryWrapper.eq("status", StatusEnum.ENABLE.getStatus());
        queryWrapper.in(CollUtil.isNotEmpty(context.getCodeList()), "code", context.getCodeList());
        queryWrapper.exists(StringUtil.isNotEmpty(context.getGroupName()), String.format("select 1 from rbp_color_group where name = %s", context.getGroupName()));
        queryWrapper.orderByAsc("updated_time");
        // 分页查询
        IPage<Color> pages = colorDao.selectPage(new Page<>(context.getPageNo(), context.getPageSize()), queryWrapper);
        if (CollUtil.isNotEmpty(pages.getRecords())) {
            List<ColorGroup> colorGroupList = colorGroupDao.selectList(new QueryWrapper<ColorGroup>().select("id,name").in("id", StreamUtil.toSet(pages.getRecords(), Color::getGroupId)));
            Map<Long, String> groupMap = OptionalUtil.ofNullable(colorGroupList, data -> data.stream().collect(Collectors.toMap(ColorGroup::getId, x -> x.getName(), (x1, x2) -> x1)));
            List<ColorData> list = new ArrayList<>();
            for (Color entity : pages.getRecords()) {
                ColorData data = new ColorData();
                data.setColorCode(entity.getCode());
                data.setColorName(entity.getName());
                data.setColorGroup(groupMap.get(entity.getGroupId()));
                list.add(data);
            }
            // 过滤字段
            if (StringUtil.isNotEmpty(context.getFields())) {
                FieldFilterTool<ColorData> tool = new FieldFilterTool();
                resultList.setData(tool.getFieldFilterList(list, context.getFields(), ColorData.class));
            } else {
                resultList.setData(list);
            }
        }

        return resultList;
    }

    /**
     * 批量创建
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse batchCreate(ColorSaveParam param) {
        ColorSaveContext context = new ColorSaveContext();
        this.convertSaveContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorData")}));
        }
        // 判断颜色编号/说明/颜组不能为空
        for (ColorData data : context.getList()) {
            if (StringUtil.isEmpty(data.getColorCode())) {
                return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorCode")}));
            } else if (StringUtil.isEmpty(data.getColorName())) {
                return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorName")}));
            } else if (StringUtil.isEmpty(data.getColorGroup())) {
                return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorGroup")}));
            }
        }
        // 判断颜色编号是否重复
        List<String> existList = new ArrayList<>();
        this.findInsertExists(existList, context.getList());
        if (CollUtil.isNotEmpty(existList)) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{String.join(StrUtil.COMMA, existList)}));
        }
        // 获取或新增颜色组
        Map<String, Long> groupMap = this.queryAndInsertColorGroupList(context.getList());
        // 批量新增颜色
        List<Color> colorList = new ArrayList<>();
        for (ColorData entity : context.getList()) {
            colorList.add(Color.build(entity.getColorCode(), entity.getColorName(), groupMap.get(entity.getColorGroup())));
        }
        this.saveBatch(colorList);
        return DataResponse.Success();
    }

    /**
     * 批量更新
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse batchUpdate(ColorUpdateParam param) {
        ColorUpdateContext context = new ColorUpdateContext();
        this.convertUpdateContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorData")}));
        }
        // 判断颜色编号/说明不能为空
        for (ColorData data : context.getList()) {
            if (StringUtil.isEmpty(data.getColorCode())) {
                return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorCode")}));
            } else if (StringUtil.isEmpty(data.getColorName())) {
                return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorName")}));
            }
        }
        // 判断颜色编号是否重复
        Map<String, ColorData> dataMap = context.getList().stream().collect(Collectors.toMap(ColorData::getColorCode, Function.identity()));
        StringBuilder existStr = new StringBuilder();
        Set<String> stringSet = new HashSet<>();
        context.getList().stream().filter(f -> !stringSet.add(f.getColorCode())).forEach(item -> existStr.append(item.getColorCode()).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 判断颜色编号是否存在
        List<Color> list = colorDao.selectList(new QueryWrapper<Color>().select("id,code").in("code", StreamUtil.toList(context.getList(), ColorData::getColorCode)));
        if (CollUtil.isEmpty(list)) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotExist",
                    new String[]{LanguageUtil.getMessage("colorCode").concat(": ").concat(String.join(StrUtil.COMMA, dataMap.keySet()))}));
        }
        List<String> codeList = list.stream().map(Color::getCode).collect(Collectors.toList());
        existStr.setLength(0);
        context.getList().stream().filter(f -> !codeList.contains(f.getColorCode())).forEach(item -> existStr.append(item.getColorCode()).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 获取或新增颜色组
        Map<String, Long> groupMap = this.queryAndInsertColorGroupList(context.getList());
        // 批量修改颜色
        list.forEach(item -> {
            ColorData colorData = dataMap.get(item.getCode());
            item.preUpdate();
            item.setName(colorData.getColorName());
            item.setGroupId(groupMap.get(colorData.getColorGroup()));
        });
        this.updateBatchById(list);

        return DataResponse.Success();
    }

    /**
     * 批量删除
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse batchDelete(ColorDeleteParam param) {
        ColorDeleteContext context = new ColorDeleteContext();
        this.convertDeleteContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getCodeList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("colorCode")}));
        }
        // 批量删除
        colorDao.delete(new QueryWrapper<Color>().in("code", context.getCodeList()));
        return DataResponse.Success();
    }

    /**
     * 查询转换器
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(ColorQueryParam param, ColorQueryContext context) {
        context.setCodeList(OptionalUtil.ofNullable(param.getColorCode(), data -> Arrays.asList(data)));
        context.setPageNo(OptionalUtil.ofNullable(param, ColorQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, ColorQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setPageSize(param.getPageSize());
    }

    /**
     * 创建转换器
     *
     * @param param
     * @param context
     */
    private void convertSaveContext(ColorSaveParam param, ColorSaveContext context) {
        context.setList(param.getColorData());
    }

    /**
     * 修改转换器
     *
     * @param param
     * @param context
     */
    private void convertUpdateContext(ColorUpdateParam param, ColorUpdateContext context) {
        context.setList(param.getColorData());
    }

    /**
     * 删除转换器
     *
     * @param param
     * @param context
     */
    private void convertDeleteContext(ColorDeleteParam param, ColorDeleteContext context) {
        context.setCodeList(OptionalUtil.ofNullable(param.getColorCode(), data -> Arrays.asList(data)));
    }


    /**
     * 查询已存在数据
     */
    private void findInsertExists(List<String> existList, List<ColorData> dataList) {
        Set<String> stringSet = new HashSet<>();
        dataList.stream().filter(f -> !stringSet.add(f.getColorCode())).forEach(item -> existList.add(item.getColorCode()));
        if (CollUtil.isEmpty(existList)) {
            List<Color> list = colorDao.selectList(new QueryWrapper<Color>().select("code").in("code", StreamUtil.toList(dataList, ColorData::getColorCode)));
            if (CollUtil.isNotEmpty(list)) {
                existList.addAll(list.stream().map(Color::getCode).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 查询并新增颜色组
     *
     * @param colorDataList
     * @return
     */
    private Map<String, Long> queryAndInsertColorGroupList(List<ColorData> colorDataList) {
        Map<String, Long> groupMap = new HashMap<>();
        Set<String> groupNameList = colorDataList.stream().map(ColorData::getColorGroup).collect(Collectors.toSet());
        List<ColorGroup> groupList = colorGroupDao.selectList(new QueryWrapper<ColorGroup>().select("id,name").in("name", groupNameList));
        List<ColorGroup> insertList = new ArrayList<>();
        if (CollUtil.isNotEmpty(groupList)) {
            // 部分创建
            Map<String, Long> existMap = groupList.stream().collect(Collectors.toMap(ColorGroup::getName, v -> v.getId(), (x1, x2) -> x1));
            groupNameList.stream().filter(f -> null == existMap.get(f)).forEach(name -> insertList.add(ColorGroup.build(name)));
            if (CollUtil.isNotEmpty(insertList)) {
                colorDao.batchInsertColorGroup(insertList);
                groupMap = insertList.stream().collect(Collectors.toMap(ColorGroup::getName, v -> v.getId(), (x1, x2) -> x1));
            }
            groupMap.putAll(existMap);
        } else {
            // 全部创建
            groupNameList.forEach(name -> insertList.add(ColorGroup.build(name)));
            colorDao.batchInsertColorGroup(insertList);
            groupMap = insertList.stream().collect(Collectors.toMap(ColorGroup::getName, v -> v.getId(), (x1, x2) -> x1));
        }

        return groupMap;
    }

}
