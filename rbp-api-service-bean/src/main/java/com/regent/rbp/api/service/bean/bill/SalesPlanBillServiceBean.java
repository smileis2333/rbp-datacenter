package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.salePlan.*;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.salePlan.*;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.SalePlanQueryParam;
import com.regent.rbp.api.dto.sale.SalePlanSaveParam;
import com.regent.rbp.api.dto.sale.SalesPlanBillGoodsResult;
import com.regent.rbp.api.dto.sale.SalesPlanBillQueryResult;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.sale.SalesPlanBillService;
import com.regent.rbp.api.service.sale.context.SalesPlanQueryContext;
import com.regent.rbp.api.service.sale.context.SalesPlanSaveContext;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesPlanBillServiceBean implements SalesPlanBillService {

    @Autowired
    private PriceTypeDao priceTypeDao;
    @Autowired
    private BusinessTypeDao businessTypeDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private CurrencyTypeDao currencyTypeDao;
    @Autowired
    private SalePlanBillDao salePlanBillDao;
    @Autowired
    private SalePlanBillGoodsDao salePlanBillGoodsDao;
    @Autowired
    private SalePlanBillGoodsFinalDao salePlanBillGoodsFinalDao;
    @Autowired
    private SalePlanBillLogisticsDao salePlanBillLogisticsDao;
    @Autowired
    private SalePlanBillSizeFinalDao salePlanBillSizeFinalDao;
    @Autowired
    private SalePlanBillSizeDao salePlanBillSizeDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private SystemCommonService systemCommonService;

    @Override
    public PageDataResponse<SalesPlanBillQueryResult> query(SalePlanQueryParam param) {
        SalesPlanQueryContext context = new SalesPlanQueryContext();
        convertQueryContext(param, context);
        return searchPage(context);
    }

    private PageDataResponse<SalesPlanBillQueryResult> searchPage(SalesPlanQueryContext context) {

        PageDataResponse<SalesPlanBillQueryResult> result = new PageDataResponse<>();
        Page<SalePlanBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = processQueryWrapper(context);

        IPage<SalePlanBill> salesPageData = salePlanBillDao.selectPage(pageModel, queryWrapper);
        List<SalesPlanBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

        result.setTotalCount(salesPageData.getTotal());
        result.setData(list);

        return result;
    }

    private List<SalesPlanBillQueryResult> convertQueryResult(List<SalePlanBill> records) {
        if (records.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> billIds = CollUtil.map(records, SalePlanBill::getId, true);
        Map<Long, SalePlanBillGoodsFinal> billGoodsMap = salePlanBillGoodsFinalDao.selectList(new QueryWrapper<SalePlanBillGoodsFinal>().in("bill_id", billIds)).stream().collect(Collectors.toMap(SalePlanBillGoodsFinal::getId, Function.identity()));
        List<SalePlanBillGoodsFinal> billGoods = billGoodsMap.values().stream().collect(Collectors.toList());
        ArrayList<Long> goodsIds = CollUtil.distinct(CollUtil.map(billGoods, SalePlanBillGoodsFinal::getGoodsId, true));
        Map<Long, List<SalePlanBillSizeFinal>> billGoodsSizeMap = salePlanBillSizeFinalDao.selectList(new QueryWrapper<SalePlanBillSizeFinal>().in("bill_id", billIds)).stream().collect(Collectors.groupingBy(SalePlanBillSizeFinal::getBillId));
        List<SalePlanBillSizeFinal> billSizes = billGoodsSizeMap.values().stream().flatMap(p -> p.stream()).collect(Collectors.toList());
        ArrayList<Long> colorIds = CollUtil.distinct(CollUtil.map(billSizes, SalePlanBillSizeFinal::getColorId, true));
        ArrayList<Long> longIds = CollUtil.distinct(CollUtil.map(billSizes, SalePlanBillSizeFinal::getLongId, true));
        Map<Long, BusinessType> businessTypeMap = businessTypeDao.selectList(new QueryWrapper<BusinessType>().in("id", CollUtil.distinct(CollUtil.map(records, SalePlanBill::getBusinessTypeId, true)))).stream().collect(Collectors.toMap(BusinessType::getId, Function.identity()));
        Map<Long, PriceType> priceTypeMap = priceTypeDao.selectList(new QueryWrapper<PriceType>().in("id", CollUtil.distinct(CollUtil.map(records, SalePlanBill::getPriceTypeId, true)))).stream().collect(Collectors.toMap(PriceType::getId, Function.identity()));
        Map<Long, String> channelMap = channelDao.selectList(new QueryWrapper<Channel>().in("id", CollUtil.distinct(CollUtil.map(records, SalePlanBill::getChannelId, true)))).stream().collect(Collectors.toMap(Channel::getId, Channel::getCode));
        ArrayList<Long> currencyTypeIds = CollUtil.distinct(CollUtil.map(records, SalePlanBill::getCurrencyTypeId, true));
        Map<Long, CurrencyType> currencyTypeMap = currencyTypeIds.isEmpty() ? new HashMap<>() : currencyTypeDao.selectList(new QueryWrapper<CurrencyType>().in("id", currencyTypeIds)).stream().collect(Collectors.toMap(CurrencyType::getId, Function.identity()));
        Map<Long, Goods> goodsMap = goodsIds.isEmpty() ? new HashMap<>() : goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, goodsIds)).stream().collect(Collectors.toMap(Goods::getId, Function.identity()));
        Map<Long, LongInfo> longMap = longIds.isEmpty() ? new HashMap<>() : longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, longIds)).stream().collect(Collectors.toMap(LongInfo::getId, Function.identity()));
        Map<Long, String> colorMap = colorIds.isEmpty() ? new HashMap<>() : colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, colorIds)).stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        Map<Long, SizeDetail> sizeDetailMap = goodsMap.isEmpty() ? new HashMap<>() : sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().in("size_class_id", goodsMap.values().stream().map(Goods::getSizeClassId).collect(Collectors.toSet()))).stream().collect(Collectors.toMap(SizeDetail::getId, Function.identity()));
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.SALE_PLAN_BILL, billIds);
        Map<Long, List<CustomizeDataDto>> billGoodsCustomDataMap = baseDbService.getCustomizeColumnMap(TableConstants.SALE_PLAN_BILL_GOODS, CollUtil.map(billGoods, SalePlanBillGoodsFinal::getId, true));


        return records.stream().map(r -> {
            SalesPlanBillQueryResult item = BeanUtil.copyProperties(r, SalesPlanBillQueryResult.class);
            item.setBusinessType(Optional.ofNullable(businessTypeMap.get(r.getBusinessTypeId())).map(BusinessType::getName).orElse(null));
            item.setPriceType(Optional.ofNullable(priceTypeMap.get(r.getPriceTypeId())).map(PriceType::getName).orElse(null));
            item.setCurrencyType(Optional.ofNullable(currencyTypeMap.get(r.getCurrencyTypeId())).map(CurrencyType::getName).orElse(null));
            item.setChannelCode(channelMap.get(r.getChannelId()));
            item.setCreatedDate(r.getCreatedTime());
            item.setUpdatedDate(r.getUpdatedTime());
            item.setCheckDate(r.getCheckTime());
            item.setCustomizeData(billCustomMap == null ? null : billCustomMap.get(r.getId()));

            List<SalesPlanBillGoodsResult> goodsDetailData =
                    billGoodsSizeMap.get(r.getId()).stream().map(bsd -> {
                        SalesPlanBillGoodsResult spbgr = new SalesPlanBillGoodsResult();
                        spbgr.setGoodsCode(Optional.ofNullable(goodsMap.get(bsd.getGoodsId())).map(Goods::getCode).orElse(null));
                        spbgr.setColorCode(colorMap.get(bsd.getColorId()));
                        spbgr.setLongName(Optional.ofNullable(longMap.get(bsd.getLongId())).map(LongInfo::getName).orElse(null));
                        spbgr.setSize(Optional.ofNullable(sizeDetailMap.get(bsd.getSizeId())).map(SizeDetail::getName).orElse(null));
                        spbgr.setQuantity(bsd.getQuantity());
                        SalePlanBillGoodsFinal bgs = billGoodsMap.get(bsd.getBillGoodsId());
                        if (bgs!=null) {
                            spbgr.setDiscount(bgs.getDiscount());
                            spbgr.setTagPrice(bgs.getTagPrice());
                            spbgr.setBalancePrice(bgs.getBalancePrice());
                            spbgr.setCurrencyPrice(bgs.getCurrencyPrice());
                            spbgr.setExchangeRate(bgs.getExchangeRate());
                            spbgr.setDeliveryDate(bgs.getDeliveryDate());
                            spbgr.setRemark(bgs.getRemark());
                        }
                        spbgr.setGoodsCustomizeData(billGoodsCustomDataMap.get(bsd.getBillGoodsId()));
                        return spbgr;
                    }).collect(Collectors.toList());
            item.setGoodsDetailData(goodsDetailData);
            return item;
        }).collect(Collectors.toList());
    }

    private QueryWrapper processQueryWrapper(SalesPlanQueryContext context) {
        QueryWrapper<SalePlanBill> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(context.getModuleId())) {
            queryWrapper.eq("module_id", context.getModuleId());
        }
        if (StrUtil.isNotEmpty(context.getBillNo())) {
            queryWrapper.eq("bill_no", context.getBillNo());
        }
        if (context.getBillDate() != null) {
            queryWrapper.eq("bill_date", context.getBillDate());
        }
        if (context.getBusinessTypeId() != null) {
            queryWrapper.eq("business_type_id", context.getBusinessTypeId());
        }
        if (context.getChannelId() != null) {
            queryWrapper.eq("channel_id", context.getChannelId());
        }
        if (StrUtil.isNotEmpty(context.getManualId())) {
            queryWrapper.eq("manual_id", context.getManualId());
        }
        if (context.getPriceTypeId() != null) {
            queryWrapper.eq("price_type_id", context.getPriceTypeId());
        }
        if (context.getCurrencyTypeId() != null) {
            queryWrapper.eq("currency_type_id", context.getCurrencyTypeId());
        }
        if (StrUtil.isNotEmpty(context.getNotes())) {
            queryWrapper.like("notes", context.getNotes());
        }
        if (CollUtil.isNotEmpty(context.getStatus())) {
            queryWrapper.in("status", context.getStatus());
        }
        if (context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if (context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if (context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if (context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if (context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if (context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }

        return queryWrapper;
    }

    private void convertQueryContext(SalePlanQueryParam param, SalesPlanQueryContext context) {
        BeanUtil.copyProperties(param, context);
        if (StrUtil.isNotEmpty(param.getBusinessType())) {
            context.setBusinessTypeId(Optional.ofNullable(businessTypeDao.selectOne(new QueryWrapper<BusinessType>().eq("name", param.getBusinessType()))).map(BusinessType::getId).orElse(null));
        }
        if (StrUtil.isNotEmpty(param.getPriceType())) {
            context.setPriceTypeId(Optional.ofNullable(priceTypeDao.selectOne(new QueryWrapper<PriceType>().eq("name", param.getPriceType()))).map(PriceType::getId).orElse(null));
        }
        if (StrUtil.isNotEmpty(param.getCurrencyType())) {
            context.setCurrencyTypeId(Optional.ofNullable(currencyTypeDao.selectOne(new QueryWrapper<CurrencyType>().eq("name", param.getCurrencyType()))).map(CurrencyType::getId).orElse(null));
        }
        if (CollUtil.isNotEmpty(param.getStatus())) {
            context.setStatus(param.getStatus());
        }

        context.setPageNo(OptionalUtil.ofNullable(param, SalePlanQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, SalePlanQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

    }

    @Override
    @Transactional
    public DataResponse save(SalePlanSaveParam param) {
        SalesPlanSaveContext context = new SalesPlanSaveContext(param);
        convertSaveContext(param, context);

        List<String> errorMsgList = validate(context);
        if (CollUtil.isNotEmpty(errorMsgList)) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        save(context);
        return DataResponse.success();
    }

    private List<String> validate(SalesPlanSaveContext context) {
        SalePlanSaveParam param = context.getParam();
        List<String> errorMsgList = new ArrayList<>();
        SalePlanBill csp = context.getSalePlanBill();
        if (!StrUtil.isEmpty(context.getPriceType()) && csp.getPriceTypeId() == null) {
            errorMsgList.add("价格类型名称(priceType)不存在");
        }
        if (StrUtil.isNotEmpty(context.getLogisticsCompanyCode()) && context.getSalePlanBillLogistics().getLogisticsCompanyId() == null) {
            errorMsgList.add("物流公司编号(logisticsCompanyCode)不存在");
        }

        List<SalesPlanBillGoodsResult> withoutBarcodeGoods = param.getGoodsDetailData().stream().filter(e -> StrUtil.isEmpty(e.getBarcode())).collect(Collectors.toList());
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getCode, withoutBarcodeGoods.stream().map(SalesPlanBillGoodsResult::getGoodsCode).collect(Collectors.toSet())));
        Integer type = goodsList.stream().filter(e -> e.getCode().equals(param.getGoodsDetailData().get(0).getGoodsCode())).findFirst().get().getType();
        Map<String, Long> longNameIdMap = type == 2 ? Collections.emptyMap() : withoutBarcodeGoods.isEmpty() ? new HashMap<>() : longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getName, withoutBarcodeGoods.stream().map(SalesPlanBillGoodsResult::getLongName).collect(Collectors.toSet()))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<String, Long> colorCodeIdMap = type == 2 ? Collections.emptyMap() : withoutBarcodeGoods.isEmpty() ? new HashMap<>() : colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getCode, withoutBarcodeGoods.stream().map(SalesPlanBillGoodsResult::getColorCode).collect(Collectors.toSet()))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Goods> goodsMap = withoutBarcodeGoods.isEmpty() ? new HashMap<>() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Function.identity()));
        List<SizeDetail> sizeClassList = type == 2 ? Collections.emptyList() : baseDbDao.getSizeNameList(StreamUtil.toList(goodsList, Goods::getId), StreamUtil.toList(param.getGoodsDetailData(), v -> v.getSize()));
        Map<String, Long> sizeMap = type == 2 ? Collections.emptyMap() : sizeClassList.stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));
        List<String> barcodes = param.getGoodsDetailData().isEmpty() ? new ArrayList<>() : param.getGoodsDetailData().stream().map(SalesPlanBillGoodsResult::getBarcode).distinct().collect(Collectors.toList());
        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? new HashMap<>() : barcodeDao.selectList(new LambdaQueryWrapper<Barcode>().in(Barcode::getBarcode, barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));

        Map<SalePlanBillGoods.GoodsIdBalancePricePair, SalePlanBillGoods> salePlanBillGoodsMap = new HashMap<>();
        Map<SalePlanBillGoods.GoodsIdBalancePricePair, List<SalePlanBillSize>> salePlanBillSizeMap = new HashMap<>();

        SalePlanBill salePlanBill = context.getSalePlanBill();
        param.getGoodsDetailData().forEach(g -> {
            if (StrUtil.isNotEmpty(g.getBarcode())) {
                Barcode barcode = barcodeMap.get(g.getBarcode());
                SalePlanBillGoods billGoods = null;
                SalePlanBillGoods.GoodsIdBalancePricePair key = new SalePlanBillGoods.GoodsIdBalancePricePair(barcode.getGoodsId(), g.getBalancePrice());
                if ((billGoods = salePlanBillGoodsMap.get(key)) == null) {
                    billGoods = new SalePlanBillGoods();
                    billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    BeanUtil.copyProperties(g, billGoods, "goodsCustomizeData");
                    billGoods.setGoodsId(barcode.getGoodsId());
                    billGoods.setBillId(salePlanBill.getId());
                    HashMap<String, Object> customData = new HashMap<>();
                    g.getGoodsCustomizeData().forEach(item -> customData.put(item.getCode(), item.getValue()));
                    customData.put("id", billGoods.getId());
                    billGoods.setGoodsCustomizeData(customData);
                    salePlanBillGoodsMap.put(key, billGoods);
                }
                SalePlanBillSize billGoodsSize = new SalePlanBillSize();
                BeanUtil.copyProperties(g, billGoodsSize);
                BeanUtil.copyProperties(barcode, billGoodsSize);
                billGoodsSize.setBillGoodsId(billGoods.getId());
                billGoodsSize.setBillId(salePlanBill.getId());
                List<SalePlanBillSize> gbc = salePlanBillSizeMap.getOrDefault(key, new ArrayList<>());
                gbc.add(billGoodsSize);
                salePlanBillSizeMap.putIfAbsent(key, gbc);
                context.addSalePlanBillSize(billGoodsSize);
            } else {
                Goods goods = null;
                SalePlanBillGoods billGoods = null;
                SalePlanBillGoods.GoodsIdBalancePricePair key = new SalePlanBillGoods.GoodsIdBalancePricePair(goods.getId(), g.getBalancePrice());
                if ((billGoods = salePlanBillGoodsMap.get(key)) == null) {
                    billGoods = new SalePlanBillGoods();
                    billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    BeanUtil.copyProperties(g, billGoods, "goodsCustomizeData");
                    billGoods.setGoodsId(goods.getId());
                    HashMap<String, Object> customData = new HashMap<>();
                    g.getGoodsCustomizeData().forEach(item -> customData.put(item.getCode(), item.getValue()));
                    customData.put("id", billGoods.getId());
                    billGoods.setGoodsCustomizeData(customData);
                    billGoods.setBillId(salePlanBill.getId());
                    salePlanBillGoodsMap.put(key, billGoods);
                }

                SalePlanBillSize billGoodsSize = new SalePlanBillSize();
                BeanUtil.copyProperties(g, billGoodsSize);
                Long billId = salePlanBill.getId();
                Long goodsId = goods.getId();
                billGoodsSize.setBillId(billId);
                billGoodsSize.setGoodsId(goodsId);
                billGoodsSize.setBillGoodsId(billGoods.getId());
                billGoodsSize.setSizeId(sizeMap.getOrDefault(billGoodsSize.getGoodsId() + StrUtil.UNDERLINE + g.getSize(), 1200000000000005L));
                billGoodsSize.setColorId(colorCodeIdMap.getOrDefault(g.getColorCode(), 1200000000000002L));
                billGoodsSize.setLongId(longNameIdMap.getOrDefault(g.getLongName(), 1200000000000003L));
                List<SalePlanBillSize> gbc = salePlanBillSizeMap.getOrDefault(key, new ArrayList<>());
                gbc.add(billGoodsSize);
                salePlanBillSizeMap.putIfAbsent(key, gbc);
                context.addSalePlanBillSize(billGoodsSize);

            }

        });
        salePlanBillGoodsMap.values().forEach(context::addSalePlanBillGoods);
        return errorMsgList;
    }

    private void convertSaveContext(SalePlanSaveParam param, SalesPlanSaveContext context) {
        context.setParam(param);
        PriceType priceType = priceTypeDao.selectOne(new QueryWrapper<PriceType>().eq("name", param.getPriceType()));
        Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getChannelCode()));
        BusinessType businessType = businessTypeDao.selectOne(new QueryWrapper<BusinessType>().eq("name", param.getBusinessType()));
        CurrencyType currencyType = currencyTypeDao.selectOne(new QueryWrapper<CurrencyType>().eq("name", param.getCurrencyType()));
        context.getSalePlanBill().setPriceTypeId(priceType != null ? priceType.getId() : null);
        context.getSalePlanBill().setChannelId(channel != null ? channel.getId() : null);
        context.getSalePlanBill().setBusinessTypeId(businessType != null ? businessType.getId() : null);
        context.getSalePlanBill().setCurrencyTypeId(currencyType != null ? currencyType.getId() : null);
        if (StrUtil.isNotEmpty(param.getLogisticsCompanyCode())) {
            context.getSalePlanBillLogistics().setLogisticsCompanyId(baseDbDao.getLongDataBySql(String.format("select id from rbp_logistics_company where status = 100 and code = '%s'", param.getLogisticsCompanyCode())));
        }
        SalePlanBill salePlanBill = context.getSalePlanBill();
        salePlanBill.setBillNo(systemCommonService.getBillNo(salePlanBill.getModuleId()));
    }

    private void save(SalesPlanSaveContext context) {
        SalePlanBill salePlanBill = context.getSalePlanBill();
        salePlanBillDao.insert(salePlanBill);
        salePlanBillLogisticsDao.insert(context.getSalePlanBillLogistics());
        context.getSalePlanBillGoodsList().forEach(bg -> salePlanBillGoodsDao.insert(bg));
        context.getSalePlanBillSizeList().forEach(bgs -> salePlanBillSizeDao.insert(bgs));
        context.getSalePlanBillGoodsList().forEach(bg -> salePlanBillGoodsFinalDao.insert(BeanUtil.copyProperties(bg, SalePlanBillGoodsFinal.class)));
        context.getSalePlanBillSizeList().forEach(bgs -> {
            SalePlanBillSizeFinal spbsf = BeanUtil.copyProperties(bgs, SalePlanBillSizeFinal.class);
            spbsf.setOweQuantity(bgs.getQuantity());
            salePlanBillSizeFinalDao.insert(spbsf);
        });
        baseDbService.saveOrUpdateCustomFieldData(salePlanBill.getModuleId(), TableConstants.SALE_PLAN_BILL, salePlanBill.getId(), context.getCustomizeData());
        baseDbService.batchSaveOrUpdateCustomFieldData(salePlanBill.getModuleId(), TableConstants.SALE_PLAN_BILL_GOODS, CollUtil.map(context.getSalePlanBillGoodsList(), SalePlanBillGoods::getGoodsCustomizeData, true));
    }
}