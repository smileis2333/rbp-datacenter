package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBillSize;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.purchaseReturnBill.PurchaseReturnBillDao;
import com.regent.rbp.api.dao.purchaseReturnBill.PurchaseReturnBillGoodsDao;
import com.regent.rbp.api.dao.purchaseReturnBill.PurchaseReturnBillSizeDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.*;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.bean.base.SystemDictionaryService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchaseReturn.PurchaseReturnBillService;
import com.regent.rbp.api.service.purchaseReturn.context.PurchaseReturnBillSaveContext;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Service
public class PurchaseReturnBillServiceBean implements PurchaseReturnBillService {
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private Validator validator;
    @Autowired
    private PurchaseReturnBillDao purchaseReturnBillDao;
    @Autowired
    private PurchaseReturnBillGoodsDao purchaseReturnillGoodsDao;
    @Autowired
    private PurchaseReturnBillSizeDao purchaseReturnBillSizeDao;
    @Autowired
    private SystemDictionaryService systemDictionaryService;

    @Override
    public PageDataResponse<PurchaseReturnBillQueryResult> query(PurchaseReturnBillQueryParam param) {
        Page<PurchaseReturnBill> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<PurchaseReturnBillQueryResult> raw = purchaseReturnBillDao.searchPageData(page, param);
        PageDataResponse<PurchaseReturnBillQueryResult> result = new PageDataResponse<>();
        result.setData(convertQueryResult(raw.getRecords()));
        result.setTotalCount(raw.getTotal());
        return result;
    }

    private List<PurchaseReturnBillQueryResult> convertQueryResult(List<PurchaseReturnBillQueryResult> records) {
        if (CollUtil.isEmpty(records)) {
            return Collections.emptyList();
        }
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(records, PurchaseReturnBillQueryResult::getModuleId, true));
        List<Long> billIds = CollUtil.map(records, PurchaseReturnBillQueryResult::getId, true);
        List<PurchaseReturnBillGoods> billGoods = purchaseReturnillGoodsDao.selectList(new LambdaQueryWrapper<PurchaseReturnBillGoods>().in(PurchaseReturnBillGoods::getBillId, billIds));
        Map<Long, String> goodsIdCodeMap = goodsDao.selectBatchIds(CollUtil.distinct(CollUtil.map(billGoods, PurchaseReturnBillGoods::getGoodsId, true))).stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        List<PurchaseReturnBillSize> billSizes = purchaseReturnBillSizeDao.selectList(new LambdaQueryWrapper<PurchaseReturnBillSize>().in(PurchaseReturnBillSize::getBillId, billIds));
        Map<Long, String> longIdNameMap = systemDictionaryService.getLongMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, PurchaseReturnBillSize::getLongId, true)), LongInfo::getId, LongInfo::getName);
        Map<Long, String> colorIdCodeMap = systemDictionaryService.getColorMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, PurchaseReturnBillSize::getColorId, true)), Color::getId, Color::getCode);
        Map<Long, String> sizeIdNameMap = systemDictionaryService.getSizeDetailMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, PurchaseReturnBillSize::getSizeId, true)), SizeDetail::getId, SizeDetail::getName);

        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RETURN_BILL, billIds);
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RETURN_BILL_GOODS, CollUtil.map(billGoods, PurchaseReturnBillGoods::getId, true));
        Map<Long, List<PurchaseReturnBillGoods>> billGoodsListMap = billGoods.stream().collect(Collectors.groupingBy(PurchaseReturnBillGoods::getBillId));
        Map<Long, List<PurchaseReturnBillSize>> billSizeListMap = billSizes.stream().collect(Collectors.groupingBy(PurchaseReturnBillSize::getBillId));
        for (PurchaseReturnBillQueryResult record : records) {
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(record.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            record.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(record.getId())));
            // 货品列表
            List<PurchaseReceiveNoticeBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            record.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<PurchaseReturnBillGoods> billGoodsList = billGoodsListMap.get(record.getId());
            Map<Long, PurchaseReturnBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(PurchaseReturnBillGoods::getId, Function.identity()));
            // 尺码明细
            List<PurchaseReturnBillSize> billSizeList = billSizeListMap.get(record.getId());
            for (PurchaseReturnBillSize size : billSizeList) {
                PurchaseReturnBillGoods bgs = currentGoodsMap.get(size.getBillGoodsId());
                PurchaseReceiveNoticeBillGoodsDetailData detailData = new PurchaseReceiveNoticeBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(bgs.getId())));
                goodsQueryResultList.add(detailData);
                detailData.setBalancePrice(bgs.getBalancePrice());
                detailData.setTagPrice(bgs.getTagPrice());
                detailData.setCurrencyPrice(bgs.getCurrencyPrice());
                detailData.setDiscount(bgs.getDiscount());
                detailData.setExchangeRate(bgs.getExchangeRate());
                detailData.setRemark(bgs.getRemark());
                detailData.setQuantity(size.getQuantity());
                detailData.setLongName(longIdNameMap.get(size.getLongId()));
                detailData.setColorCode(colorIdCodeMap.get(size.getColorId()));
                detailData.setSize(sizeIdNameMap.get(size.getSizeId()));
                detailData.setGoodsCode(goodsIdCodeMap.get(size.getGoodsId()));
            }

        }
        return records;
    }

    @Override
    @Transactional
    public DataResponse save(PurchaseReturnBillSaveParam param) {
        PurchaseReturnBillSaveContext context = new PurchaseReturnBillSaveContext();
        List<String> messageList = new ArrayList<>();
        convertSaveContext(context, param, messageList);
        if (!messageList.isEmpty()) {
            return ModelDataResponse.errorParameter(messageList.stream().collect(Collectors.joining(",")));
        }
        purchaseReturnBillDao.insert(context.getBill());
        context.getBillGoodsList().forEach(purchaseReturnillGoodsDao::insert);
        context.getBillSizeList().forEach(purchaseReturnBillSizeDao::insert);
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RETURN_BILL, context.getBill().getId(), context.getBillCustomizeDataDtos());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RETURN_BILL_GOODS, context.getGoodsCustomizeData());
        return DataResponse.success();
    }

    private void convertSaveContext(PurchaseReturnBillSaveContext context, PurchaseReturnBillSaveParam param, List<String> messageList) {
        PurchaseReturnBill bill = BeanUtil.copyProperties(param, PurchaseReturnBill.class);
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.setBillNo(systemCommonService.getBillNo(param.getModuleId()));
        bill.setProcessStatus(0);
        bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        bill.setSupplierId(baseDbDao.getLongDataBySql(String.format("select id from rbp_supplier where status = 1 and code = '%s'", param.getSupplierCode())));
        if (StrUtil.isNotEmpty(param.getPurchaseNo())) {
            bill.setPurchaseId(baseDbDao.getLongDataBySql(String.format("select id from rbp_purchase_bill where bill_no  = '%s'", param.getPurchaseNo())));
        }
        if (StringUtil.isNotEmpty(param.getChannelCode())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
        }
        if (StringUtil.isNotEmpty(param.getNoticeNo())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_purchase_return_notice_bill where status = 1 and code = '%s'", param.getChannelCode())));
        }
        if (StrUtil.isNotBlank(param.getCurrencyType())) {
            bill.setCurrencyTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_currency_type where status = 100 and name = '%s'", param.getCurrencyType())));
        }
        context.setBill(bill);
        extractedGoodsAndSizes(context, param, messageList);
        context.setBillCustomizeDataDtos(param.getCustomizeData());
    }

    private void extractedGoodsAndSizes(PurchaseReturnBillSaveContext context, PurchaseReturnBillSaveParam param, List<String> messageList) {
        List<PurchaseReceiveNoticeBillGoodsDetailData> goodsDetailData = param.getGoodsDetailData();
        List<String> barcodes = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getBarcode()) ? e.getBarcode() : null, true);
        List<String> goodsCode = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getGoodsCode()) ? e.getGoodsCode() : null, true);
        List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsCode));
        Integer type = goodsList.stream().filter(e -> e.getCode().equals(param.getGoodsDetailData().get(0).getGoodsCode())).findFirst().get().getType();
        List<Long> goodsIdsList = CollUtil.map(goodsList, Goods::getId, true);
        List<String> colorCodes = type == 2 ? Collections.emptyList() : CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getColorCode, true));
        List<String> longNames = type == 2 ? Collections.emptyList() : CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getLongName, true));
        List<String> sizeNames = type == 2 ? Collections.emptyList() : CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getSize, true); // don't distinct, must align

        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        Map<String, Long> goodsMap = goodsIdsList.isEmpty() ? Collections.emptyMap() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = systemDictionaryService.getColorMapFromCodes(colorCodes, Color::getCode, Color::getId);
        Map<String, Long> longMap = systemDictionaryService.getLongMapFromNames(longNames, LongInfo::getName, LongInfo::getId);
        Map<Long, Map<String, Long>> goodsSizeNameMap = type == 2 ? Collections.emptyMap() : systemDictionaryService.getSizeDetailPartitionMapFromSizeNamesAndGoodsCodes(sizeNames, goodsCode, SizeDetail::getGoodsId, SizeDetail::getName, SizeDetail::getId);
        goodsDetailData.forEach(e -> {
            if (StrUtil.isNotBlank(e.getBarcode())) {
                Barcode barcode = null;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    e.setGoodsId(barcode.getGoodsId());
                    e.setColorId(barcode.getColorId());
                    e.setLongId(barcode.getLongId());
                    e.setSizeId(barcode.getSizeId());
                    e.setBarcodeId(barcode.getId());
                }
            } else {
                e.setGoodsId(goodsMap.get(e.getGoodsCode()));
                e.setColorId(colorMap.getOrDefault(e.getColorCode(), 1200000000000002L));
                e.setLongId(longMap.getOrDefault(e.getLongName(), 1200000000000003L));
                e.setSizeId(goodsSizeNameMap.getOrDefault(e.getGoodsId(), Collections.emptyMap()).getOrDefault(e.getSize(), 1200000000000005L));
            }
        });

        // 根据货品+价格分组，支持同款多价
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(GoodsDetailIdentifier::getSameGoodsDiffPriceKey)).forEach((key, sizes) -> {
            PurchaseReturnBillGoods billGoods = BeanUtil.copyProperties(sizes.get(0), PurchaseReturnBillGoods.class);
            billGoods.setQuantity(sizes.stream().map(PurchaseReceiveNoticeBillGoodsDetailData::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
            context.addBillGoods(billGoods);
            context.addGoodsDetailCustomData(sizes.get(0).getGoodsCustomizeData(), billGoods.getId());
            sizes.forEach(size -> {
                PurchaseReturnBillSize billSize = BeanUtil.copyProperties(size, PurchaseReturnBillSize.class);
                billSize.setBillGoodsId(billGoods.getId());
                context.addBillSize(billSize);
            });
        });

    }

}
