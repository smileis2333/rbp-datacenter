package com.regent.rbp.api.service.bean.barcode;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dto.base.BarcodeItem;
import com.regent.rbp.api.dto.base.BarcodeQueryParam;
import com.regent.rbp.api.dto.base.BarcodeQueryResult;
import com.regent.rbp.api.dto.base.BarcodeSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.barcode.BarcodeService;
import com.regent.rbp.api.service.barcode.context.BarcodeQueryContext;
import com.regent.rbp.api.service.barcode.context.BarcodeSaveContext;
import com.regent.rbp.common.service.basic.SystemCommonService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Service
public class BarcodeServiceBean implements BarcodeService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    Validator validator;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Override
    public PageDataResponse<BarcodeQueryResult> searchPageData(BarcodeQueryParam param) {
        BarcodeQueryContext context = new BarcodeQueryContext();
        convertQueryContext(param, context);
        if (ObjectUtils.anyNotNull(param.getGoodsCode(), param.getGoodsName(), param.getMnemonicCode()) && context.getGoodsIds().isEmpty()) {
            return new PageDataResponse<>(0, Collections.emptyList());
        }
        QueryWrapper barcodeQueryWrapper = processQueryWrapper(context);
        Page<Barcode> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        Page<Barcode> barcodePage = barcodeDao.selectPage(pageModel, barcodeQueryWrapper);
        List<BarcodeQueryResult> cooked = convertQueryResult(barcodePage.getRecords());
        PageDataResponse<BarcodeQueryResult> res = new PageDataResponse<>();
        res.setData(cooked);
        res.setTotalCount(barcodePage.getTotal());
        return res;
    }

    private QueryWrapper processQueryWrapper(BarcodeQueryContext context) {
        QueryWrapper<Barcode> barcodeQueryWrapper = new QueryWrapper<>();
        barcodeQueryWrapper.in("goods_id", context.getGoodsIds());
        barcodeQueryWrapper.eq(StrUtil.isNotEmpty(context.getRuleId()), "rule_id", context.getRuleId());
        barcodeQueryWrapper.in(CollUtil.isNotEmpty(context.getBarcode()), "barcode", context.getBarcode());
        return barcodeQueryWrapper;
    }

    private List<BarcodeQueryResult> convertQueryResult(List<Barcode> raw) {
        ArrayList<Long> goodsIds = CollUtil.distinct(CollUtil.map(raw, Barcode::getGoodsId, true));
        ArrayList<Long> colorIds = CollUtil.distinct(CollUtil.map(raw, Barcode::getColorId, true));
        ArrayList<Long> longIds = CollUtil.distinct(CollUtil.map(raw, Barcode::getLongId, true));
        ArrayList<Long> sizeIds = CollUtil.distinct(CollUtil.map(raw, Barcode::getSizeId, true));

        Map<Long, Goods> goodsMap = goodsDao.selectList(new QueryWrapper<Goods>().in("id", goodsIds)).stream().collect(Collectors.toMap(Goods::getId, Function.identity()));
        Map<Long, Color> colorMap = colorDao.selectList(new QueryWrapper<Color>().in("id", colorIds)).stream().collect(Collectors.toMap(Color::getId, Function.identity()));
        Map<Long, LongInfo> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("id", longIds)).stream().collect(Collectors.toMap(LongInfo::getId, Function.identity()));
        Map<Long, SizeDetail> sizeMap = sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().in("id", sizeIds)).stream().collect(Collectors.toMap(SizeDetail::getId, Function.identity()));

        return raw.stream().map(e -> {
            BarcodeQueryResult c = BeanUtil.copyProperties(e, BarcodeQueryResult.class);
            Goods goods = goodsMap.get(e.getGoodsId());
            c.setGoodsCode(goods.getCode());
            c.setGoodsName(goods.getName());
            c.setMnemonicCode(goods.getMnemonicCode());
            c.setColorCode(colorMap.get(e.getColorId()).getCode());
            c.setLongName(longMap.get(e.getLongId()).getName());
            c.setSize(sizeMap.get(e.getSizeId()).getName());
            return c;
        }).collect(Collectors.toList());

    }

    private void convertQueryContext(BarcodeQueryParam param, BarcodeQueryContext context) {
        BeanUtil.copyProperties(param,context);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotEmpty(param.getGoodsCode()), "code", param.getGoodsCode());
        wrapper.like(StrUtil.isNotEmpty(param.getGoodsName()), "name", param.getGoodsName());
        wrapper.eq(StrUtil.isNotEmpty(param.getMnemonicCode()), "mnemonic_code", param.getMnemonicCode());
        List<Goods> goods = goodsDao.selectList(wrapper);
        if (!goods.isEmpty()) {
            context.setGoodsIds(CollUtil.distinct(CollUtil.map(goods, Goods::getId, true)));
        }
    }

    @Override
    @Transactional
    public DataResponse batchCreate(BarcodeSaveParam param) {
        BarcodeSaveContext context = new BarcodeSaveContext();
        List<String> messages = convertSaveContext(context, param);
        if (CollUtil.isNotEmpty(messages)) {
            return ModelDataResponse.errorParameter(StrUtil.join(",", messages));
        }
        context.getBarcodes().forEach(e -> {
            if (e.getId() != null) {
                barcodeDao.updateById(e);
            } else {
                e.setId(systemCommonService.getId());
                barcodeDao.insert(e);
            }
        });

        return DataResponse.success();
    }

    private List<String> convertSaveContext(BarcodeSaveContext context, BarcodeSaveParam param) {
        List<String> messages = new ArrayList<>();

        List<BarcodeItem> barcodeData = param.getBarcodeData();
        List<String> goodCodes = barcodeData.stream().map(BarcodeItem::getGoodsCode).distinct().collect(Collectors.toList());
        List<String> longNames = barcodeData.stream().map(BarcodeItem::getLongName).distinct().collect(Collectors.toList());
        List<String> colorCodes = barcodeData.stream().map(BarcodeItem::getColorCode).distinct().collect(Collectors.toList());

        Map<String, Goods> goodsMap = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodCodes)).stream().collect(Collectors.toMap(Goods::getCode, Function.identity()));
        Map<String, Color> colorMap = colorDao.selectList(new QueryWrapper<Color>().in("code", colorCodes)).stream().collect(Collectors.toMap(Color::getCode, Function.identity()));
        Map<String, LongInfo> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", longNames)).stream().collect(Collectors.toMap(LongInfo::getName, Function.identity()));

        // align
        ArrayList<Long> goodsIdForSize = new ArrayList<>();
        ArrayList<String> sizeNames = new ArrayList<>();
        barcodeData.forEach(e -> {
            Goods goods = null;
            if ((goods = goodsMap.get(e.getGoodsCode())) != null) {
                goodsIdForSize.add(goods.getId());
                sizeNames.add(e.getSize());
            }
        });
        List<SizeDetail> sizeDetailList = null;
        if (!goodsIdForSize.isEmpty())
            sizeDetailList = baseDbDao.getSizeNameList(goodsIdForSize, sizeNames);
        List<SizeDetail> finalSizeDetailList = sizeDetailList;
        barcodeData.forEach(e -> {
            if (goodsMap.containsKey(e.getGoodsCode())) {
                e.setGoodsId(goodsMap.get(e.getGoodsCode()).getId());
            }
            if (colorMap.containsKey(e.getColorCode())) {
                e.setColorId(colorMap.get(e.getColorCode()).getId());
            }
            if (longMap.containsKey(e.getLongName())) {
                e.setLongId(longMap.get(e.getLongName()).getId());
            }
            if (e.getGoodsId() != null) {
                SizeDetailHelper.getSizeDetail(finalSizeDetailList, e.getGoodsId(), e.getSize()).ifPresent(s -> e.setSizeId(s.getId()));
            }
        });
        Set<ConstraintViolation<BarcodeSaveParam>> validateResult = validator.validate(param, Complex.class);
        if (!validateResult.isEmpty()) {
            messages = validateResult.stream().map(e -> e.getMessage()).collect(Collectors.toList());
        } else {
            List<Barcode> barcodes = barcodeData.stream().map(e -> BeanUtil.copyProperties(e, Barcode.class)).collect(Collectors.toList());
            context.setBarcodes(barcodes);
            Map<String, Barcode> existBarcodesMap = barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", CollUtil.map(barcodes, Barcode::getBarcode, true))).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
            barcodes.forEach(e -> {
                Barcode barcode = null;
                if ((barcode = existBarcodesMap.get(e.getBarcode())) != null) {
                    e.setId(barcode.getId());
                }
            });

        }

        return messages;
    }

    static class SizeDetailHelper {
        public static Optional<SizeDetail> getSizeDetail(List<SizeDetail> sizeClassList, Long goodsId, String sizeName) {
            return sizeClassList.stream().filter(e -> !sizeClassList.isEmpty() && goodsId != null && e.getGoodsId() == goodsId.longValue() && StrUtil.equals(e.getName(), sizeName)).findFirst();
        }
    }
}
