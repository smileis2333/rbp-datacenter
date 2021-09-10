package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dto.base.LongData;
import com.regent.rbp.api.dto.base.LongDeleteParam;
import com.regent.rbp.api.dto.base.LongQueryParam;
import com.regent.rbp.api.dto.base.LongSaveParam;
import com.regent.rbp.api.dto.base.LongUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.LongService;
import com.regent.rbp.api.service.base.context.LongDeleteContext;
import com.regent.rbp.api.service.base.context.LongQueryContext;
import com.regent.rbp.api.service.base.context.LongSaveContext;
import com.regent.rbp.api.service.base.context.LongUpdateContext;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 内长
 */
@Service
public class LongServiceBean extends ServiceImpl<LongDao, LongInfo> implements LongService {

    @Autowired
    private LongDao longDao;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<String> searchPageData(LongQueryParam param) {
        LongQueryContext context = new LongQueryContext();
        this.convertQueryContext(param, context);
        // 分页
        IPage<LongInfo> pages = longDao.selectPage(new Page<>(SystemConstants.PAGE_NO, SystemConstants.BATCH_SIZE),
                new QueryWrapper<LongInfo>().select("name").in(CollUtil.isNotEmpty(context.getLongNameList()), "name", context.getLongNameList()));
        List<String> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(pages.getRecords())) {
            list = pages.getRecords().stream().map(LongInfo::getName).collect(Collectors.toList());
        }

        return new PageDataResponse<>(pages.getTotal(), list);
    }

    /**
     * 批量创建
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse batchCreate(LongSaveParam param) {
        LongSaveContext context = new LongSaveContext();
        this.convertSaveContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getLongNameList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("longName")}));
        }
        // 判断内长是否重复
        Set<String> set = new HashSet<>();
        StringBuilder existStr = new StringBuilder();
        context.getLongNameList().stream().filter(f -> !set.add(f)).forEach(v -> existStr.append(v).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        existStr.setLength(0);
        List<LongInfo> list = longDao.selectList(new QueryWrapper<LongInfo>().select("name").in("name", context.getLongNameList()));
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(v -> existStr.append(v).append(StrUtil.COMMA));
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 批量新增内长
        List<LongInfo> insertList = new ArrayList<>();
        context.getLongNameList().forEach(name -> {
            LongInfo entity = LongInfo.build(name);
            insertList.add(entity);
        });
        this.saveBatch(insertList);

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
    public DataResponse batchUpdate(LongUpdateParam param) {
        LongUpdateContext context = new LongUpdateContext();
        this.convertUpdateContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getLongList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("longName")}));
        }
        // 判断内长是否重复
        Set<String> set = new HashSet<>();
        StringBuilder existStr = new StringBuilder();
        context.getLongList().stream().filter(f -> !set.add(f.getOriginalLongName())).forEach(v -> existStr.append(v.getOriginalLongName()).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        set.clear();
        existStr.setLength(0);
        context.getLongList().stream().filter(f -> !set.add(f.getLongName())).forEach(v -> existStr.append(v.getLongName()).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 判断原始内长是否存在
        List<LongInfo> list = longDao.selectList(new QueryWrapper<LongInfo>().select("id,name").in("name", StreamUtil.toList(context.getLongList(), LongData::getOriginalLongName)));
        if (CollUtil.isEmpty(list)) {
            existStr.setLength(0);
            context.getLongList().stream().forEach(v -> existStr.append(v.getOriginalLongName()).append(StrUtil.COMMA));
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotExist", new String[]{existStr.toString()}));
        }
        if (context.getLongList().size() > list.size()) {
            existStr.setLength(0);
            List<String> nameList = list.stream().map(LongInfo::getName).collect(Collectors.toList());
            context.getLongList().stream().filter(f -> !nameList.contains(f.getOriginalLongName())).forEach(v -> existStr.append(v.getOriginalLongName()).append(StrUtil.COMMA));
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 批量修改内长
        Map<String, String> longMap = context.getLongList().stream().collect(Collectors.toMap(LongData::getOriginalLongName, v -> v.getLongName(), (x1, x2) -> x1));
        list.forEach(item -> {
            item.setName(longMap.get(item.getName()));
            item.preUpdate();
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
    public DataResponse batchDelete(LongDeleteParam param) {
        LongDeleteContext context = new LongDeleteContext();
        this.convertDeleteContext(param, context);
        // 参数验证
        if (CollUtil.isEmpty(context.getLongNameList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("longName")}));
        }
        // 批量删除
        longDao.delete(new QueryWrapper<LongInfo>().in("name", context.getLongNameList()));
        return DataResponse.Success();
    }

    /**
     * 查询转换器
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(LongQueryParam param, LongQueryContext context) {
        context.setLongNameList((OptionalUtil.ofNullable(param.getLongName(), data -> Arrays.asList(data))));
    }

    /**
     * 创建转换器
     *
     * @param param
     * @param context
     */
    private void convertSaveContext(LongSaveParam param, LongSaveContext context) {
        context.setLongNameList((OptionalUtil.ofNullable(param.getLongName(), data -> Arrays.asList(data))));

    }

    /**
     * 修改转换器
     *
     * @param param
     * @param context
     */
    private void convertUpdateContext(LongUpdateParam param, LongUpdateContext context) {
        context.setLongList((OptionalUtil.ofNullable(param.getLongData(), data -> Arrays.asList(data))));
    }

    /**
     * 删除转换器
     *
     * @param param
     * @param context
     */
    private void convertDeleteContext(LongDeleteParam param, LongDeleteContext context) {
        context.setLongNameList((OptionalUtil.ofNullable(param.getLongName(), data -> Arrays.asList(data))));
    }


}
