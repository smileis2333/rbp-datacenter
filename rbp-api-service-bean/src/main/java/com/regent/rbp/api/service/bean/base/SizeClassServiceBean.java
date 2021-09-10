package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.SizeClass;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.dao.base.SizeClassDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dto.base.SizeClassData;
import com.regent.rbp.api.dto.base.SizeClassDeleteParam;
import com.regent.rbp.api.dto.base.SizeClassQueryParam;
import com.regent.rbp.api.dto.base.SizeClassSaveParam;
import com.regent.rbp.api.dto.base.SizeClassUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.SizeClassService;
import com.regent.rbp.api.service.base.context.SizeClassDeleteContext;
import com.regent.rbp.api.service.base.context.SizeClassQueryContext;
import com.regent.rbp.api.service.base.context.SizeClassSaveContext;
import com.regent.rbp.api.service.base.context.SizeClassUpdateContext;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.constants.ResponseCode;
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
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码
 */
@Service
public class SizeClassServiceBean extends ServiceImpl<SizeClassDao, SizeClass> implements SizeClassService {

    @Autowired
    private SizeClassDao sizeClassDao;

    @Autowired
    private SizeDetailDao sizeDetailDao;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<SizeClassData> searchPageData(SizeClassQueryParam param) {
        SizeClassQueryContext context = new SizeClassQueryContext();
        this.convertQueryContext(param, context);
        // 分页
        IPage<SizeClass> pages = sizeClassDao.selectPage(new Page<>(context.getPageNo(), context.getPageSize()), new QueryWrapper<SizeClass>().in("name", context.getSizeClassNameList()));
        List<SizeClassData> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(pages.getRecords())) {
            // 查询尺码
            Map<Long, List<SizeDetail>> sizeDetailMap = new HashMap<>();
            List<SizeDetail> sizeDetailList = sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().select("name")
                    .in("size_class_id", StreamUtil.toList(pages.getRecords(), SizeClass::getId)).orderByAsc("order_number"));
            if (CollUtil.isNotEmpty(sizeDetailList)) {
                sizeDetailMap = sizeDetailList.stream().collect(Collectors.groupingBy(SizeDetail::getSizeClassId));
            }
            for (SizeClass item : pages.getRecords()) {
                SizeClassData data = new SizeClassData();
                data.setSizeClassName(item.getName());
                List<SizeDetail> details = sizeDetailMap.get(item.getId());
                if (CollUtil.isNotEmpty(details)) {
                    data.setSize(details.stream().map(SizeDetail::getName).collect(Collectors.toList()).toArray(new String[0]));
                }
                list.add(data);
            }
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
    public DataResponse batchCreate(SizeClassSaveParam param) {
        SizeClassSaveContext context = new SizeClassSaveContext();
        this.convertSaveContext(param, context);
        // 参数验证
        if (StringUtil.isEmpty(context.getSizeClassName())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        if (CollUtil.isEmpty(context.getSizeList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("size")}));
        }
        SizeClass sizeClass = sizeClassDao.selectOne(new QueryWrapper<SizeClass>().eq("name", context.getSizeClassName()).last("limit 1"));
        if (null == sizeClass) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataExist", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        // 判断尺码是否重复
        Set<String> set = new HashSet<>();
        StringBuilder existStr = new StringBuilder();
        context.getSizeList().stream().filter(f -> !set.add(f)).forEach(v -> existStr.append(v).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 新建尺码类别
        SizeClass entity = SizeClass.build(context.getSizeClassName());
        sizeClassDao.insert(entity);
        // 批量新建尺码
        this.fillSizeDetailList(entity.getId(), context.getSizeList(), false);

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
    public DataResponse batchUpdate(SizeClassUpdateParam param) {
        SizeClassUpdateContext context = new SizeClassUpdateContext();
        this.convertUpdateContext(param, context);
        // 参数验证
        if (StringUtil.isEmpty(context.getSizeClassName())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        if (CollUtil.isEmpty(context.getSizeList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("size")}));
        }
        SizeClass sizeClass = sizeClassDao.selectOne(new QueryWrapper<SizeClass>().eq("name", context.getSizeClassName()).last("limit 1"));
        if (null == sizeClass) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataExist", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        // 判断尺码是否重复
        Set<String> set = new HashSet<>();
        StringBuilder existStr = new StringBuilder();
        context.getSizeList().stream().filter(f -> !set.add(f)).forEach(v -> existStr.append(v).append(StrUtil.COMMA));
        if (existStr.length() > 0) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{existStr.toString()}));
        }
        // 更新尺码类别
        sizeClass.preUpdate();
        sizeClassDao.updateById(sizeClass);
        // 批量新建尺码
        this.fillSizeDetailList(sizeClass.getId(), context.getSizeList(), false);
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
    public DataResponse batchDelete(SizeClassDeleteParam param) {
        SizeClassDeleteContext context = new SizeClassDeleteContext();
        this.convertDeleteContext(param, context);
        // 参数验证
        if (StringUtil.isEmpty(context.getSizeClassName())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        SizeClass sizeClass = sizeClassDao.selectOne(new QueryWrapper<SizeClass>().eq("name", context.getSizeClassName()));
        if (null == sizeClass) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotExist", new String[]{LanguageUtil.getMessage("sizeClassName")}));
        }
        // 批量删除
        sizeDetailDao.delete(new QueryWrapper<SizeDetail>().in("size_class_id", sizeClass.getId()));
        sizeClassDao.deleteById(sizeClass.getId());
        return DataResponse.Success();
    }

    /**
     * 查询转换器
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(SizeClassQueryParam param, SizeClassQueryContext context) {
        context.setSizeClassNameList((OptionalUtil.ofNullable(param.getSizeClassName(), data -> Arrays.asList(data))));
        context.setPageNo(OptionalUtil.ofNullable(param, SizeClassQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, SizeClassQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setPageSize(param.getPageSize());
    }

    /**
     * 创建转换器
     *
     * @param param
     * @param context
     */
    private void convertSaveContext(SizeClassSaveParam param, SizeClassSaveContext context) {
        context.setSizeList((OptionalUtil.ofNullable(param.getSize(), data -> Arrays.asList(data))));
        context.setSizeClassName(param.getSizeClassName());
    }

    /**
     * 修改转换器
     *
     * @param param
     * @param context
     */
    private void convertUpdateContext(SizeClassUpdateParam param, SizeClassUpdateContext context) {
        context.setSizeList((OptionalUtil.ofNullable(param.getSize(), data -> Arrays.asList(data))));
        context.setSizeClassName(param.getSizeClassName());
    }

    /**
     * 删除转换器
     *
     * @param param
     * @param context
     */
    private void convertDeleteContext(SizeClassDeleteParam param, SizeClassDeleteContext context) {
        context.setSizeClassName(param.getSizeClassName());
    }

    /**
     * 批量创建尺码
     *
     * @param sizeClassId
     * @param sizeDetailList
     * @param isUpdate       是否更新
     */
    private void fillSizeDetailList(Long sizeClassId, List<String> sizeDetailList, boolean isUpdate) {
        if (isUpdate) {
            sizeDetailDao.delete(new QueryWrapper<SizeDetail>().eq("size_class_id", sizeClassId));
        }
        List<SizeDetail> details = new ArrayList<>();
        int i = 1;
        for (String name : sizeDetailList) {
            SizeDetail detail = SizeDetail.build(name, sizeClassId);
            detail.setOrderNumber(i);
            detail.setFieldName("s" + i);
            details.add(detail);
            i++;
        }
        sizeClassDao.batchInsertSizeDetail(details);
    }

}
