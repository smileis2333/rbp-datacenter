package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.receipt.Receipt;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.receipt.ReceiptDao;
import com.regent.rbp.api.dto.base.BaseGoodsPriceDto;
import com.regent.rbp.api.dto.base.GoodsDetailData;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.bean.bill.ReceiptBillServiceBean;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.task.yumei.dao.YumeiReceiptBillGoodsDao;
import com.regent.rbp.task.yumei.dao.YumeiReceiptBillSizeDao;
import com.regent.rbp.task.yumei.model.YumeiReceiptBillGoodsDetailData;
import com.regent.rbp.task.yumei.model.YumeiReceiptBillSaveParam;
import com.regent.rbp.task.yumei.model.entity.YumeiReceiptBillGoods;
import com.regent.rbp.task.yumei.model.entity.YumeiReceiptBillSize;
import com.regent.rbp.task.yumei.service.YumeiReceiptBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Service
public class YumeiReceiptBillServiceBean extends ReceiptBillServiceBean implements YumeiReceiptBillService {
    @Autowired
    private YumeiReceiptBillGoodsDao receiptBillGoodsDao;
    @Autowired
    private YumeiReceiptBillSizeDao receiptBillSizeDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private BaseDbService baseDbService;

    @Override
    @Transactional
    public DataResponse customSave(YumeiReceiptBillSaveParam param) {
        param.setCurrencyAmount(BigDecimal.ZERO);
        ModelDataResponse<String> save = (ModelDataResponse) super.save(param);

        Receipt bill = receiptDao.selectOne(Wrappers.lambdaQuery(Receipt.class).eq(Receipt::getBillNo, save.getData()));

        List<YumeiReceiptBillSize> billSizes = new ArrayList<>();
        List<YumeiReceiptBillGoods> billGoodsList = new ArrayList<>();

        List<YumeiReceiptBillGoodsDetailData> goodsDetailData = param.getGoodsDetailData();
        List<String> barcodes = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getBarcode()) ? e.getBarcode() : null, true);
        List<String> goodsCode = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getGoodsCode()) ? e.getGoodsCode() : null, true);

        List<Goods> goodsList = CollUtil.isEmpty(goodsCode) ? Collections.emptyList() : goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsCode));
        List<Long> goodsIdsList = CollUtil.map(goodsList, Goods::getId, true);
        List<String> colorCodes = CollUtil.distinct(CollUtil.map(goodsDetailData, GoodsDetailData::getColorCode, true));
        List<String> longNames = CollUtil.distinct(CollUtil.map(goodsDetailData, GoodsDetailData::getLongName, true));
        List<String> sizeNames = CollUtil.map(goodsDetailData, GoodsDetailData::getSize, true); // don't distinct, must align
        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        Map<String, Long> goodsCodeIdMap = goodsIdsList.isEmpty() ? Collections.emptyMap() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getCode, Function.identity()));
        Map<Long, BaseGoodsPriceDto> priceMap = null;
        if (CollUtil.isNotEmpty(barcodeMap)){
            priceMap = baseDbService.getBaseGoodsPriceMapByGoodsIds(barcodeMap.values().stream().map(Barcode::getGoodsId).collect(Collectors.toList()));
        }else if (CollUtil.isNotEmpty(goodsList)){
            priceMap = baseDbService.getBaseGoodsPriceMapByGoodsIds(goodsIdsList);
        }
        Integer type = null;
        if (StrUtil.isNotBlank(goodsDetailData.get(0).getGoodsCode())) {
            type = goodsMap.get(goodsDetailData.get(0).getGoodsCode()).getType();
        }

        Map<String, Long> colorMap = Collections.emptyMap();
        Map<String, Long> longMap = Collections.emptyMap();
        Map<String, Long> sizeMap = Collections.emptyMap();
        if (type != null && type == 1) {
            if (CollUtil.isNotEmpty(colorCodes)) {
                colorMap = colorDao.selectList(new QueryWrapper<Color>().in("code", colorCodes)).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
            }
            if (CollUtil.isNotEmpty(longNames)) {
                longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", longNames)).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
            }
            if (CollUtil.isNotEmpty(sizeNames)) {
                sizeMap = baseDbDao.getSizeNameList(goodsIdsList, sizeNames).stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));
            }
        }

        for (YumeiReceiptBillGoodsDetailData e : goodsDetailData) {
            YumeiReceiptBillSize billSize = new YumeiReceiptBillSize();
            if (StrUtil.isNotBlank(e.getBarcode())) {
                Barcode barcode = null;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    billSize.setGoodsId(barcode.getGoodsId());
                    billSize.setColorId(barcode.getColorId());
                    billSize.setLongId(barcode.getLongId());
                    billSize.setSizeId(barcode.getSizeId());
                    billSize.setQuantity(e.getQuantity());
                    BaseGoodsPriceDto price = priceMap.get(barcode.getGoodsId());
                    if (price!=null) {
                        e.setTagPrice(price.getTagPrice());
                        e.setDiscount(e.getBalancePrice().divide(e.getTagPrice()));
                    }
                }
            } else {
                billSize.setGoodsId(goodsCodeIdMap.get(e.getGoodsCode()));
                billSize.setColorId(colorMap.getOrDefault(e.getColorCode(), 1200000000000002L));
                billSize.setLongId(longMap.getOrDefault(e.getLongName(), 1200000000000003L));
                billSize.setSizeId(sizeMap.getOrDefault(billSize.getGoodsId() + StrUtil.UNDERLINE + e.getSize(), 1200000000000005L));
                billSize.setQuantity(e.getQuantity());
                BaseGoodsPriceDto price = priceMap.get(billSize.getGoodsId());
                if (price!=null) {
                    e.setTagPrice(price.getTagPrice());
                    e.setDiscount(e.getBalancePrice().divide(e.getTagPrice()));
                }
            }

            YumeiReceiptBillGoods billGoods = BeanUtil.copyProperties(e, YumeiReceiptBillGoods.class);
            billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            billGoods.setBillId(bill.getId());
            billGoods.setGoodsId(billSize.getGoodsId());

            billSize.setBillId(bill.getId());
            billSize.setBillGoodsId(billGoods.getId());
            billSizes.add(billSize);
            billGoodsList.add(billGoods);
        }

        billGoodsList.forEach(receiptBillGoodsDao::insert);
        billSizes.forEach(receiptBillSizeDao::insert);
        return save;
    }
}
