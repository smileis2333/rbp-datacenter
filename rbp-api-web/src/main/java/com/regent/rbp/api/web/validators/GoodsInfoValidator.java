package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dto.base.GoodsDetailData;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author huangjie
 * @date : 2022/03/14
 * @description
 */
@Component
public class GoodsInfoValidator implements ConstraintValidator<GoodsInfo, List<? extends GoodsDetailData>> {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public boolean isValid(List<? extends GoodsDetailData> goodsInfos, ConstraintValidatorContext constraintValidatorContext) {
        if (CollUtil.isEmpty(goodsInfos)) {
            return true;
        }
        boolean pass = true;
        HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        // close default constraint violation because we generate more detail violation
        context.disableDefaultConstraintViolation();

        AcceptMode mode = getAcceptMode(goodsInfos, context);
        if (mode == null) {
            return false;
        }

        if (mode == AcceptMode.BARCODE) {
            Set<String> barcodes = goodsInfos.stream().map(GoodsDetailData::getBarcode).collect(Collectors.toSet());
            Map<String, Long> barcodeCodeIdMap = Collections.emptyMap();
            if (CollUtil.isNotEmpty(barcodes)) {
                barcodeCodeIdMap = barcodeDao.selectList(Wrappers.lambdaQuery(Barcode.class).in(Barcode::getBarcode, barcodes)).stream().collect(toMap(Barcode::getBarcode, Barcode::getId));
            }
            for (int i = 0; i < goodsInfos.size(); i++) {
                GoodsDetailData item = goodsInfos.get(i);
                if (StrUtil.isNotEmpty(item.getBarcode()) && !barcodeCodeIdMap.containsKey(item.getBarcode())) {
                    pass = false;
                    context.addExpressionVariable("validatedValue", item.getBarcode());
                    context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("barcode").inIterable().atIndex(i).addConstraintViolation();
                }
            }
        } else if (mode == AcceptMode.DETAIL_INFO) {
            List<String> goodsCodes = CollUtil.distinct(CollUtil.map(goodsInfos, GoodsDetailData::getGoodsCode, true));
            Map<String, Long> goodsCodeIdMap = Collections.emptyMap();
            Map<String, Goods> goodsCodeGoodsMap = Collections.emptyMap();
            if (CollUtil.isNotEmpty(goodsCodes)) {
                List<Goods> goods = goodsDao.selectList(Wrappers.lambdaQuery(Goods.class).in(Goods::getCode, goodsCodes));
                goodsCodeIdMap = goods.stream().collect(toMap(Goods::getCode, Goods::getId));
                goodsCodeGoodsMap = goods.stream().collect(toMap(Goods::getCode, Function.identity()));
            }

            for (int i = 0; i < goodsInfos.size(); i++) {
                GoodsDetailData item = goodsInfos.get(i);
                if (StrUtil.isNotEmpty(item.getGoodsCode()) && !goodsCodeIdMap.containsKey(item.getGoodsCode())) {
                    pass = false;
                    context.addExpressionVariable("validatedValue", item.getGoodsCode());
                    context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("goodsCode").inIterable().atIndex(i).addConstraintViolation();
                    if (StrUtil.isNotEmpty(item.getSize())) {
                        context.addExpressionVariable("validatedValue", item.getSize());
                        context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("size").inIterable().atIndex(i).addConstraintViolation();
                    }
                }
            }

            if(!pass){
                return false;
            }

            GoodsType goodsType = getGoodsType(goodsInfos, goodsCodeGoodsMap, context);
            if (goodsType == null) {
                return false;
            }

            if (goodsType == GoodsType.NORMAL) {
                List<String> colorCodes = CollUtil.distinct(CollUtil.map(goodsInfos, GoodsDetailData::getColorCode, true));
                List<String> longNames = CollUtil.distinct(CollUtil.map(goodsInfos, GoodsDetailData::getLongName, true));
                Map<String, Long> colorCodeIdMap = Collections.emptyMap();
                Map<String, Long> longNameIdMap = Collections.emptyMap();
                if (CollUtil.isNotEmpty(colorCodes)) {
                    List<Color> colors = colorDao.selectList(Wrappers.lambdaQuery(Color.class).in(Color::getCode, colorCodes));
                    colorCodeIdMap = colors.stream().collect(toMap(Color::getCode, Color::getId));
                }
                if (CollUtil.isNotEmpty(longNames)) {
                    longNameIdMap = longDao.selectList(Wrappers.lambdaQuery(LongInfo.class).in(LongInfo::getName, longNames)).stream().collect(toMap(LongInfo::getName, LongInfo::getId));
                }
                // require not blank param
                for (int i = 0; i < goodsInfos.size(); i++) {
                    GoodsDetailData item = goodsInfos.get(i);
                    if (StrUtil.isBlank(item.getColorCode())) {
                        pass = false;
                        context.buildConstraintViolationWithTemplate("{javax.validation.constraints.NotBlank.message}").addPropertyNode("colorCode").inIterable().atIndex(i).addConstraintViolation();
                    }
                    if (StrUtil.isBlank(item.getLongName())) {
                        pass = false;
                        context.buildConstraintViolationWithTemplate("{javax.validation.constraints.NotBlank.message}").addPropertyNode("longName").inIterable().atIndex(i).addConstraintViolation();
                    }
                    if (StrUtil.isBlank(item.getSize())) {
                        pass = false;
                        context.buildConstraintViolationWithTemplate("{javax.validation.constraints.NotBlank.message}").addPropertyNode("size").inIterable().atIndex(i).addConstraintViolation();
                    }
                }

                // check for exist
                for (int i = 0; i < goodsInfos.size(); i++) {
                    GoodsDetailData item = goodsInfos.get(i);
                    if (StrUtil.isNotEmpty(item.getColorCode()) && !colorCodeIdMap.containsKey(item.getColorCode())) {
                        pass = false;
                        context.addExpressionVariable("validatedValue", item.getColorCode());
                        context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("colorCode").inIterable().atIndex(i).addConstraintViolation();
                    }
                    if (StrUtil.isNotEmpty(item.getLongName()) && !longNameIdMap.containsKey(item.getLongName())) {
                        pass = false;
                        context.addExpressionVariable("validatedValue", item.getLongName());
                        context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("longName").inIterable().atIndex(i).addConstraintViolation();
                    }
                }

                // special case about size, because it must
                // 1. align sizeName and goodsCode
                // 2. goodsCode must be valid as its precondition
                // 3. run as goodsCode mode
                if (pass) {
                    // for align then query size map
                    List<Long> goodsIds = new ArrayList<>();
                    List<String> sizeNames = new ArrayList<>();
                    for (GoodsDetailData item : goodsInfos) {
                        if (StrUtil.isNotEmpty(item.getSize())) {
                            sizeNames.add(item.getSize());
                            goodsIds.add(goodsCodeIdMap.get(item.getGoodsCode()));
                        }
                    }
                    Map<Long, Map<String, Long>> sizeDetailMap = baseDbDao.getSizeNameList(goodsIds, sizeNames).stream().collect(Collectors.groupingBy(SizeDetail::getGoodsId, Collectors.collectingAndThen(toMap(SizeDetail::getName, SizeDetail::getId), Collections::unmodifiableMap)));
                    for (int i = 0; i < goodsInfos.size(); i++) {
                        Long goodsId = null;
                        GoodsDetailData item = goodsInfos.get(i);
                        goodsId = goodsCodeIdMap.get(item.getGoodsCode());
                        if (!sizeDetailMap.containsKey(goodsId) || !sizeDetailMap.get(goodsId).containsKey(item.getSize())) {
                            pass = false;
                            context.addExpressionVariable("validatedValue", item.getSize());
                            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("size").inIterable().atIndex(i).addConstraintViolation();
                        }
                    }
                }
            }
        }

        return pass;
    }

    /**
     * 确认物料类型并处理相关信息
     */
    private GoodsType getGoodsType(List<? extends GoodsDetailData> goodsInfos, Map<String, Goods> goodsCodeGoodsMap, HibernateConstraintValidatorContext context) {
        String firstGoodsCode = goodsInfos.get(0).getGoodsCode();
        GoodsType expectGoodsType = goodsCodeGoodsMap.get(firstGoodsCode).getType() == 1 ? GoodsType.NORMAL : GoodsType.SINGLE_MATERIAL;

        boolean expectGoodsTypeCheck = true;
        for (int i = 1; i < goodsInfos.size(); i++) {
            GoodsDetailData item = goodsInfos.get(i);
            Goods goods = goodsCodeGoodsMap.get(item.getGoodsCode());
            if ((expectGoodsType == GoodsType.NORMAL && goods.getType() == 2) ||
                    expectGoodsType == GoodsType.SINGLE_MATERIAL && goods.getType() == 1) {
                expectGoodsTypeCheck = false;
                context.buildConstraintViolationWithTemplate(String.format("当前模式: %s，请更换货品类型", expectGoodsType.getDescription())).addBeanNode().inIterable().atIndex(i).addConstraintViolation();
            }
        }

        if (!expectGoodsTypeCheck) {
            context.buildConstraintViolationWithTemplate(String.format("请检查传入货品类型，不允许混合单一物料和普通物料", expectGoodsType.getDescription())).addConstraintViolation();
            return null;
        }

        return expectGoodsType;
    }

    /**
     * 获取输入模式和相关信息
     */
    private AcceptMode getAcceptMode(List<? extends GoodsDetailData> goodsInfos, HibernateConstraintValidatorContext context) {
        GoodsDetailData firstItem = goodsInfos.get(0);
        if (StrUtil.isAllBlank(firstItem.getGoodsCode(), firstItem.getBarcode()) || StrUtil.isAllNotBlank(firstItem.getGoodsCode(), firstItem.getBarcode())) {
            context.buildConstraintViolationWithTemplate("{regent.barcodeOrGoodsCode.onlyAllowOne}，模式由第一个指定，如goodsDetailData[0].barcode/goodsCode='abc'").addConstraintViolation();
            return null;
        }

        AcceptMode expectMode = StrUtil.isNotEmpty(firstItem.getBarcode()) ? AcceptMode.BARCODE : AcceptMode.DETAIL_INFO;

        boolean modeCheck = true;

        for (int i = 0; i < goodsInfos.size(); i++) {
            GoodsDetailData item = goodsInfos.get(i);
            if (expectMode == AcceptMode.DETAIL_INFO) {
                if (StrUtil.isNotBlank(item.getBarcode())) {
                    modeCheck = false;
                    context.buildConstraintViolationWithTemplate("当前模式: 货号，barcode应该为空").addBeanNode().inIterable().atIndex(i).addConstraintViolation();
                } else if (StrUtil.isBlank(item.getGoodsCode())) {
                    modeCheck = false;
                    context.buildConstraintViolationWithTemplate("{javax.validation.constraints.NotBlank.message}").addPropertyNode("goodsCode").inIterable().atIndex(i).addConstraintViolation();
                }
            } else if (expectMode == AcceptMode.BARCODE) {
                if (StrUtil.isNotBlank(item.getGoodsCode())) {
                    modeCheck = false;
                    context.buildConstraintViolationWithTemplate("当前模式: 条形码，goodsCode应该为空").addBeanNode().inIterable().atIndex(i).addConstraintViolation();
                } else if (StrUtil.isBlank(item.getBarcode())) {
                    modeCheck = false;
                    context.buildConstraintViolationWithTemplate("{javax.validation.constraints.NotBlank.message}").addPropertyNode("barcode").inIterable().atIndex(i).addConstraintViolation();
                }
            }
        }
        if (!modeCheck) {
            context.buildConstraintViolationWithTemplate(String.format("请检查输入模式，模式以第一个为准，当前模式: %s", expectMode.getDescription())).addConstraintViolation();
            return null;
        }

        return expectMode;
    }
}

enum AcceptMode {
    BARCODE("条形码"),
    DETAIL_INFO("货号"),
    ;

    private String description;

    AcceptMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

enum GoodsType {
    NORMAL("普通物料"),
    SINGLE_MATERIAL("单一物料"),
    ;
    private String description;

    GoodsType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
