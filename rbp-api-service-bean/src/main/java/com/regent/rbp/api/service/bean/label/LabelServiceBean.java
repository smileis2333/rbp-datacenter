package com.regent.rbp.api.service.bean.label;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.label.Label;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.label.LabelDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.label.LabelQueryParam;
import com.regent.rbp.api.dto.label.LabelSaveParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.bean.base.SystemDictionaryService;
import com.regent.rbp.api.service.bean.bill.ValidateMessageUtil;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.label.LabelService;
import com.regent.rbp.api.service.label.context.LabelSaveContext;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

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
    private Validator validator;
    @Autowired
    private BaseDbService baseDbService;

    @Override
    public PageDataResponse<PurchaseReturnBillQueryResult> query(LabelQueryParam param) {
        return null;
    }

    @Override
    public DataResponse save(LabelSaveParam param) {
        List<String> messages = new ArrayList<>();
        LabelSaveContext context = new LabelSaveContext();
        convertSaveContext(context, param, messages);
        saveOrUpdate(context.getLabel());
        baseDbService.saveCustomFieldData(TableConstants.LABEL,context.getLabel().getId(),context.getCustomizeData());
        return DataResponse.success();
    }

    private void convertSaveContext(LabelSaveContext context, LabelSaveParam param, List<String> messages) {

        Label label = BeanUtil.copyProperties(param, Label.class);

        Label exist = labelDao.selectByCode(param.getCode());
        if (exist!=null){
            label.setId(exist.getId());
        }else {
            label.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        }
        Goods goods = goodsDao.selectByCode(param.getCode());
        if (goods!=null){
            param.setGoodsId(param.getGoodsId());
            systemDictionaryService.getSizeDetailFromSizeNameAndGoodsCode(param.getSize(),param.getGoodsCode()).ifPresent(sizeDetail->param.setSizeId(sizeDetail.getId()));
        }
        systemDictionaryService.getColorFromCode(param.getColorCode()).ifPresent(color->param.setColorId(color.getId()));
        systemDictionaryService.getLongFromName(param.getLongName()).ifPresent(longInfo->param.setLongId(longInfo.getId()));
        if (!ValidateMessageUtil.pass(validator.validate(param, Complex.class), messages)) return;
        context.setCustomizeData(param.getCustomizeData());
    }
}
