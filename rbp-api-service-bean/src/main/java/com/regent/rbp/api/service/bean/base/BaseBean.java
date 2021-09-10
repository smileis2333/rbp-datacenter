package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.dao.base.BrandDao;
import com.regent.rbp.api.dto.base.BaseData;
import com.regent.rbp.api.dto.base.BaseQueryParam;
import com.regent.rbp.api.dto.base.BaseSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.BaseService;
import com.regent.rbp.api.service.base.context.BaseQueryContext;
import com.regent.rbp.api.service.base.context.BaseSaveContext;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.enums.BaseDataEnum;
import com.regent.rbp.api.service.utils.FieldFilterTool;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description
 */
@Service
public class BaseBean implements BaseService {

    @Autowired
    private BrandDao brandDao;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<BaseData> searchPageData(BaseQueryParam param) {
        BaseQueryContext context = new BaseQueryContext();
        // 类型转换
        this.convertQueryContext(param, context);
        // 获取基础资料表名
        String tableName = BaseDataEnum.getTableName(context.getType());
        if (StringUtils.isEmpty(tableName)) {
            return new PageDataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotExist", new String[]{LanguageUtil.getMessage("baseDataType")}));
        }
        // 查询
        Page<BaseData> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        IPage<BaseData> pages = brandDao.searchPageData(pageModel, tableName, context.getKeyword());
        // 过滤不需要code基础资料模块
        if (!BaseDataEnum.isCodeFlag(context.getType()) && CollUtil.isNotEmpty(pages.getRecords())) {
            FieldFilterTool<BaseData> tool = new FieldFilterTool();
            pages.setRecords(tool.getFieldFilterList(pages.getRecords(), "code", BaseData.class));
        }

        return new PageDataResponse<>(pages.getTotal(), pages.getRecords());
    }

    /**
     * 创建
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse batchCreate(BaseSaveParam param) {
        BaseSaveContext context = new BaseSaveContext();
        // 类型转换
        this.convertSaveContext(param, context);
        // 获取基础资料表名
        String tableName = BaseDataEnum.getTableName(context.getType());
        if (StringUtils.isEmpty(tableName)) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotExist", new String[]{LanguageUtil.getMessage("baseDataType")}));
        }
        if (CollUtil.isEmpty(context.getList())) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataNotNull", new String[]{LanguageUtil.getMessage("baseDataList")}));
        }
        // 判断是否重复
        List<String> existList = new ArrayList<>();
        Boolean codeFlag = BaseDataEnum.isCodeFlag(context.getType());
        if (codeFlag) {
            this.findExists(tableName, codeFlag, existList, context.getList());
        } else {
            this.findExists(tableName, codeFlag, existList, context.getList());
        }
        if (CollUtil.isNotEmpty(existList)) {
            return new DataResponse(ResponseCode.PARAMS_ERROR, LanguageUtil.getMessage("dataRepeated", new String[]{String.join(StrUtil.COMMA, existList)}));
        }
        // 批量新增
        List<Brand> list = new ArrayList<>();
        int i = 1;
        for (BaseData baseData : context.getList()) {
            i++;
            list.add(Brand.build(codeFlag ? baseData.getCode() : StrUtil.EMPTY, baseData.getName()));
            if (i % SystemConstants.BATCH_SIZE == 0 || i == context.getList().size()) {
                brandDao.batchInsert(tableName, list);
                list.clear();
            }
        }

        return DataResponse.Success();
    }

    /**
     * 查询转换器
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(BaseQueryParam param, BaseQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, BaseQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, BaseQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setType(param.getType());
        context.setKeyword(param.getData());
    }

    /**
     * 新增转换器
     *
     * @param param
     * @param context
     */
    private void convertSaveContext(BaseSaveParam param, BaseSaveContext context) {
        context.setType(param.getType());
        context.setList(OptionalUtil.ofNullable(param, BaseSaveParam::getData, new ArrayList<>()));
    }

    /**
     * 查询已存在数据
     *
     * @param tableName
     * @param codeFlag
     * @param existList
     * @param baseDataList
     */
    private void findExists(String tableName, Boolean codeFlag, List<String> existList, List<BaseData> baseDataList) {
        Set<String> stringSet = new HashSet<>();
        for (BaseData item : baseDataList) {
            if (codeFlag && !stringSet.add(item.getCode())) {
                existList.add(item.getCode());
            } else if (!codeFlag && !stringSet.add(item.getName())) {
                existList.add(item.getName());
            }
        }
        if (CollUtil.isEmpty(existList)) {
            List<String> list = brandDao.getExistBaseDataList(tableName, codeFlag ? "code" : "name", StreamUtil.toList(baseDataList, BaseData::getCode));
            if (CollUtil.isNotEmpty(list)) {
                existList.addAll(list);
            }
        }
    }

}
