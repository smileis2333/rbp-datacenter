package com.regent.rbp.api.service.bean.label;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.label.Label;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.label.LabelDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.label.LabelQueryParam;
import com.regent.rbp.api.dto.label.LabelQueryResult;
import com.regent.rbp.api.dto.label.LabelSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.bean.base.SystemDictionaryService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.label.LabelService;
import com.regent.rbp.api.service.label.context.LabelSaveContext;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Service
public class LabelServiceBean extends ServiceImpl<LabelDao, Label> implements LabelService  {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public PageDataResponse<LabelQueryResult> query(LabelQueryParam param) {
        Page<Label> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<LabelQueryResult> raw = labelDao.searchPageData(page, param);
        PageDataResponse<LabelQueryResult> result = new PageDataResponse<>();
        result.setData(convertQueryResult(raw.getRecords()));
        result.setTotalCount(raw.getTotal());
        return result;
    }

    private List<LabelQueryResult> convertQueryResult(List<LabelQueryResult> records) {
        if (CollUtil.isEmpty(records)){
            return records;
        }
        return records;
    }

    @Override
    public DataResponse save(LabelSaveParam param) {
        List<String> messages = new ArrayList<>();
        LabelSaveContext context = new LabelSaveContext();
        convertSaveContext(context, param, messages);
        if (!messages.isEmpty()) {
            return ModelDataResponse.errorParameter(messages.stream().collect(Collectors.joining(",")));
        }
        saveOrUpdate(context.getLabel());
        baseDbService.saveCustomFieldData(TableConstants.LABEL,context.getLabel().getId(),context.getCustomizeData());
        return DataResponse.success();
    }

    private void convertSaveContext(LabelSaveContext context, LabelSaveParam param, List<String> messages) {
        Label label = BeanUtil.copyProperties(param, Label.class);
        Goods goods = goodsDao.selectByCode(param.getGoodsCode());
        if (goods!=null){
            label.setGoodsId(goods.getId());
        }
        if (StrUtil.isNotBlank(param.getBatchManagementNo())){
            label.setBatchManagementId(baseDbDao.getLongDataBySql(String.format("select id from rbp_batch_management  where code = '%s'", param.getBatchManagementNo())));
        }
        if (StrUtil.isNotBlank(param.getLabelRuleNo())){
            label.setLabelRuleId(baseDbDao.getLongDataBySql(String.format("select id from rbp_label_rule where name = '%s'", param.getLabelRuleNo())));
        }
        if (StrUtil.isNotBlank(param.getChannelCode())){
            label.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where code = '%s'", param.getChannelCode())));
        }
        systemDictionaryService.getColorFromCode(param.getColorCode()).ifPresent(color->label.setColorId(color.getId()));
        systemDictionaryService.getLongFromName(param.getLongName()).ifPresent(longInfo->label.setLongId(longInfo.getId()));
        SizeDetail sizeDetail = systemDictionaryService.getSizeDetailFromSizeNameAndGoodsCode(param.getSize(), param.getGoodsCode()).orElseThrow(() -> new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{"size"}));
        label.setSizeId(sizeDetail.getId());
        label.setExportStatus(0);
        context.setLabel(label);
        Label exist = labelDao.selectByCode(param.getCode());
        if (exist!=null){
            label.setId(exist.getId());
        }else {
            label.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        }
        context.setCustomizeData(param.getCustomizeData());
    }
}
