package com.regent.rbp.api.service.bean.box;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.box.Box;
import com.regent.rbp.api.core.box.BoxDetail;
import com.regent.rbp.api.core.box.DistributionType;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.box.BoxDao;
import com.regent.rbp.api.dao.box.BoxDetailDao;
import com.regent.rbp.api.dao.box.DistributionTypeDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.box.*;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.bean.bill.ValidateMessageUtil;
import com.regent.rbp.api.service.box.BoxService;
import com.regent.rbp.api.service.box.context.BoxSaveContext;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Service
public class BoxServiceBean implements BoxService {
    @Autowired
    private BoxDao boxDao;
    @Autowired
    private BoxDetailDao boxDetailDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private DistributionTypeDao distributionTypeDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private Validator validator;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public PageDataResponse<BoxQueryResult> query(BoxQueryParam param) {
        Page<Box> pageModel = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<BoxQueryResult> boxIPage = boxDao.searchPageData(pageModel, param);
        List<BoxQueryResult> list = convertQueryResult(boxIPage.getRecords());
        PageDataResponse<BoxQueryResult> result = new PageDataResponse<>();
        result.setTotalCount(boxIPage.getTotal());
        result.setData(list);
        return result;
    }

    private List<BoxQueryResult> convertQueryResult(List<BoxQueryResult> records) {
        if (CollUtil.isEmpty(records)) {
            return records;
        }
        List<Long> boxIds = CollUtil.map(records, BoxQueryResult::getId, true);
        Map<Long, List<BoxDetailItem>> boxDetailMap = boxDetailDao.searchList(boxIds).stream().collect(Collectors.groupingBy(BoxDetailItem::getBoxId));
        records.forEach(box -> {
            box.setBoxDetail(boxDetailMap.get(box.getId()));
        });
        return records;
    }

    @Override
    @Transactional
    public DataResponse save(BoxSaveParam param) {
        BoxSaveContext context = new BoxSaveContext();
        ArrayList<String> messages = new ArrayList<>();
        convertSaveContext(context, param, messages);
        if (CollUtil.isNotEmpty(messages)) {
            return ModelDataResponse.errorParameter(messages.stream().collect(Collectors.joining("; ")));
        }

        List<Box> boxes = context.getBox();
        List<Long> existBoxIds = new ArrayList<>();
        boxes.forEach(box -> {
            if (box.getId() == null) {
                box.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                box.getBoxDetails().forEach(detail -> detail.setBoxId(box.getId()));
                existBoxIds.add(box.getId());
                boxDao.insert(box);
            } else {
                boxDao.updateById(box);
            }
        });

        if (CollUtil.isNotEmpty(existBoxIds)) {
            boxDetailDao.delete(Wrappers.<BoxDetail>lambdaQuery().in(BoxDetail::getBoxId, existBoxIds));
        }
        boxes.stream().map(Box::getBoxDetails).flatMap(Collection::stream).forEach(boxDetailDao::insert);

        return DataResponse.success();
    }

    private void convertSaveContext(BoxSaveContext context, BoxSaveParam param, ArrayList<String> messages) {

        List<BoxItem> boxData = param.getBoxData();
        List<String> boxCodes = CollUtil.distinct(CollUtil.map(boxData, BoxItem::getCode, true));
        List<String> manualIds = CollUtil.distinct(CollUtil.map(boxData, BoxItem::getManualId, true));
        List<String> channelCodes = CollUtil.distinct(CollUtil.map(boxData, BoxItem::getChannelCode, true));
        List<String> supplierCodes = CollUtil.distinct(CollUtil.map(boxData, BoxItem::getSupplierCode, true));
        List<String> distributionTypeCodes = CollUtil.distinct(CollUtil.map(boxData, BoxItem::getDistributionTypeCode, true));

        Map<String, Long> boxCodeIdMap = boxDao.selectList(Wrappers.lambdaQuery(Box.class).in(Box::getCode, boxCodes)).stream().collect(Collectors.toMap(Box::getCode, Box::getId));
        Map<String, Long> channelCodeIdMap = CollUtil.isEmpty(channelCodes) ? Collections.emptyMap() : channelDao.selectList(Wrappers.lambdaQuery(Channel.class).in(Channel::getCode, channelCodes)).stream().collect(Collectors.toMap(Channel::getCode, Channel::getId));
        Map<String, Long> supplierCodeIdMap = CollUtil.isEmpty(supplierCodes) ? Collections.emptyMap() : supplierDao.selectList(Wrappers.lambdaQuery(Supplier.class).in(Supplier::getCode, supplierCodes)).stream().collect(Collectors.toMap(Supplier::getCode, Supplier::getId));
        Map<String, Long> distributionTypeCodeIdMap = CollUtil.isEmpty(distributionTypeCodes) ? Collections.emptyMap() : distributionTypeDao.selectList(Wrappers.lambdaQuery(DistributionType.class).in(DistributionType::getCode, distributionTypeCodes)).stream().collect(Collectors.toMap(DistributionType::getCode, DistributionType::getId));

        // inject box config
        boxData.forEach(b -> {
            if (StrUtil.isNotBlank(b.getChannelCode())) {
                b.setChannelId(channelCodeIdMap.get(b.getChannelCode()));
            }
            if (StrUtil.isNotBlank(b.getSupplierCode())) {
                b.setSupplierId(supplierCodeIdMap.get(b.getSupplierCode()));
            }
            if (StrUtil.isNotBlank(b.getDistributionTypeCode())) {
                b.setDistributionTypeId(distributionTypeCodeIdMap.get(b.getDistributionTypeCode()));
            }
        });

        // inject goods info
        List<BoxDetailItem> boxDetails = boxData.stream().map(BoxItem::getBoxDetail).flatMap(Collection::stream).collect(Collectors.toList());
        boolean runBarcodeAsGoodInfoSource = StringUtils.isNotBlank(boxData.get(0).getBoxDetail().get(0).getBarcode());
        if (runBarcodeAsGoodInfoSource) {
            List<String> barcodes = boxDetails.stream().map(BoxDetailItem::getBarcode).distinct().collect(Collectors.toList());
            Map<String, Barcode> barcodeMap = CollUtil.isEmpty(barcodes) ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
            boxDetails.forEach(e -> {
                Barcode barcode;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    e.setGoodsId(barcode.getGoodsId());
                    e.setColorId(barcode.getColorId());
                    e.setLongId(barcode.getLongId());
                    e.setSizeId(barcode.getSizeId());
                    e.setBarcodeId(barcode.getId());
                }
            });
        } else {
            // 货品+颜色+内长+尺码
            List<String> goodsCode = boxDetails.stream().map(BoxDetailItem::getGoodsCode).distinct().collect(Collectors.toList());
            List<String> sizeNames = boxDetails.stream().map(BoxDetailItem::getSize).distinct().collect(Collectors.toList());
            Map<String, Long> goodsCodeIdMap =  goodsDao.selectList(Wrappers.<Goods>lambdaQuery().in(Goods::getCode, goodsCode)).stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
            Map<String, Long> colorCodeIdMap = colorDao.selectList(Wrappers.<Color>lambdaQuery().in(Color::getCode, boxDetails.stream().map(BoxDetailItem::getColorCode).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
            Map<String, Long> longNameIdMap = longDao.selectList(Wrappers.<LongInfo>lambdaQuery().in(LongInfo::getName, boxDetails.stream().map(BoxDetailItem::getLongName).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
            Map<Long, Map<String, Long>> goodsIdSizeNameIdMap = baseDbDao.getGoodsIdSizeNameIdMap(CollUtil.map(boxDetails,BoxDetailItem::getGoodsCode,true), sizeNames, goodsCodeIdMap);

            boxDetails.forEach(e -> {
                e.setGoodsId(goodsCodeIdMap.get(e.getGoodsCode()));
                e.setColorId(colorCodeIdMap.get(e.getColorCode()));
                e.setLongId(longNameIdMap.get(e.getLongName()));
                e.setSizeId(goodsIdSizeNameIdMap.getOrDefault(e.getGoodsId(), Collections.emptyMap()).get(e.getSize()));
            });
        }

        // manual trigger validate
        if (!ValidateMessageUtil.pass(validator.validate(param, Complex.class), messages)) return;

        boxData.forEach(b -> {
            Box box = new Box();
            BeanUtil.copyProperties(b, box, "boxDetails");
            if (boxCodeIdMap.containsKey(b.getCode())) {
                box.setId(boxCodeIdMap.get(b.getCode()));
            }

            b.getBoxDetail().forEach(d -> {
                BoxDetail boxDetail = BeanUtil.copyProperties(d, BoxDetail.class);
                boxDetail.setBoxId(box.getId());
                boxDetail.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                box.getBoxDetails().add(boxDetail);
            });

            context.getBox().add(box);
        });
    }
}
