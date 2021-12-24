package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillSize;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillDao;
import com.regent.rbp.api.dao.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillGoodsDao;
import com.regent.rbp.api.dao.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillSizeDao;
import com.regent.rbp.api.dao.salePlan.BusinessTypeDao;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchase.PurchaseReceiveNoticeBillService;
import com.regent.rbp.api.service.purchase.context.PurchaseReceiveNoticeBillQueryContext;
import com.regent.rbp.api.service.purchase.context.PurchaseReceiveNoticeBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Service
public class PurchaseReceiveNoticeBillServiceBean implements PurchaseReceiveNoticeBillService {
    @Autowired
    private PurchaseReceiveNoticeBillDao purchaseReceiveNoticeBillDao;
    @Autowired
    private PurchaseReceiveNoticeBillGoodsDao purchaseReceiveNoticeBillGoodsDao;
    @Autowired
    private PurchaseReceiveNoticeBillSizeDao purchaseReceiveNoticeBillSizeDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private DbService dbService;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private BusinessTypeDao businessTypeDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private CurrencyTypeDao currencyTypeDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private PurchaseBillDao purchaseBillDao;

    @Override
    public PageDataResponse<PurchaseReceiveNoticeBillQueryResult> query(PurchaseReceiveNoticeBillQueryParam param) {
        PurchaseReceiveNoticeBillQueryContext context = new PurchaseReceiveNoticeBillQueryContext();
        convertQueryContext(param, context);
        Page<PurchaseReceiveNoticeBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = processQueryWrapper(context);
        IPage<PurchaseReceiveNoticeBill> receiveBillIPage = purchaseReceiveNoticeBillDao.selectPage(pageModel, queryWrapper);

        List<PurchaseReceiveNoticeBillQueryResult> list = convertQueryResult(receiveBillIPage.getRecords());
        PageDataResponse<PurchaseReceiveNoticeBillQueryResult> result = new PageDataResponse<>();
        result.setTotalCount(receiveBillIPage.getTotal());
        result.setData(list);
        return result;
    }

    private List<PurchaseReceiveNoticeBillQueryResult> convertQueryResult(List<PurchaseReceiveNoticeBill> list) {
        List<PurchaseReceiveNoticeBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(PurchaseReceiveNoticeBill::getId).distinct().collect(Collectors.toList());
        // 收货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, PurchaseReceiveNoticeBill::getToChannelId);
        Set<Long> supplierIds = StreamUtil.toSet(list, PurchaseReceiveNoticeBill::getSupplierId);
        channelIds.addAll(StreamUtil.toSet(list, PurchaseReceiveNoticeBill::getToChannelId));
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        Map<Long, IdNameCodeDto> supplierMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Supplier>().in("id",supplierIds ), Supplier.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, PurchaseReceiveNoticeBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, PurchaseReceiveNoticeBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<PurchaseReceiveNoticeBillSize> purchaseReceiveNoticeBillSizes = purchaseReceiveNoticeBillSizeDao.selectList(new LambdaQueryWrapper<PurchaseReceiveNoticeBillSize>().in(PurchaseReceiveNoticeBillSize::getBillId, billIdList));
        List<PurchaseReceiveNoticeBillGoods> purchaseReceiveNoticeBillGoods = purchaseReceiveNoticeBillGoodsDao.selectList(new LambdaQueryWrapper<PurchaseReceiveNoticeBillGoods>().in(PurchaseReceiveNoticeBillGoods::getBillId, billIdList));
        // 根据单据分组
        Map<Long, List<PurchaseReceiveNoticeBillSize>> billSizeMap = purchaseReceiveNoticeBillSizes.stream().collect(Collectors.groupingBy(PurchaseReceiveNoticeBillSize::getBillId));
        Map<Long, List<PurchaseReceiveNoticeBillGoods>> billGoodsMap = purchaseReceiveNoticeBillGoods.stream().collect(Collectors.groupingBy(PurchaseReceiveNoticeBillGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(purchaseReceiveNoticeBillGoods, PurchaseReceiveNoticeBillGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(purchaseReceiveNoticeBillSizes, PurchaseReceiveNoticeBillSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(purchaseReceiveNoticeBillSizes, PurchaseReceiveNoticeBillSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(purchaseReceiveNoticeBillSizes, PurchaseReceiveNoticeBillSize::getSizeId)));
        Map<Long, String> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeDetail::getId, SizeDetail::getName));
        // 条码,默认取第一个
        List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>()
                .in(CollUtil.isNotEmpty(goodsMap.keySet()), "goods_id", goodsMap.keySet())
                .in(CollUtil.isNotEmpty(colorMap.keySet()), "color_id", colorMap.keySet())
                .in(CollUtil.isNotEmpty(longMap.keySet()), "long_id", longMap.keySet())
                .in(CollUtil.isNotEmpty(sizeMap.keySet()), "size_id", sizeMap.keySet())
                .orderByDesc("barcode").groupBy("goods_id,color_id,long_id,size_id"));
        Map<String, String> barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getSingleCode, Barcode::getBarcode));
        // 模块自定义字段定义
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(list, PurchaseReceiveNoticeBill::getModuleId, true));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RECEIVE_NOTICE_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RECEIVE_NOTICE_BILL_GOODS, CollUtil.map(purchaseReceiveNoticeBillGoods, PurchaseReceiveNoticeBillGoods::getId, true));
        // 填充
        for (PurchaseReceiveNoticeBill bill : list) {
            PurchaseReceiveNoticeBillQueryResult queryResult = new PurchaseReceiveNoticeBillQueryResult();
            queryResults.add(queryResult);

            queryResult.setModuleId(bill.getModuleId());
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setToChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getToChannelId()), IdNameCodeDto::getCode));
            queryResult.setSupplierCode(OptionalUtil.ofNullable(supplierMap.get(bill.getSupplierId()), IdNameCodeDto::getCode));
            queryResult.setBusinessType(OptionalUtil.ofNullable(businessTypeMap.get(bill.getBusinessTypeId()), IdNameDto::getName));
            queryResult.setCurrencyType(OptionalUtil.ofNullable(currencyTypeMap.get(bill.getCurrencyTypeId()), IdNameDto::getName));
            queryResult.setStatus(bill.getStatus());
            queryResult.setNotes(bill.getNotes());
            queryResult.setBillDate(bill.getBillDate());
            queryResult.setCheckTime(bill.getCheckTime());
            queryResult.setCreatedTime(bill.getCreatedTime());
            queryResult.setUpdatedTime(bill.getUpdatedTime());
            queryResult.setTaxRate(bill.getTaxRate());
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(bill.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            queryResult.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(bill.getId())));
            // 货品列表
            List<PurchaseReceiveNoticeBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<PurchaseReceiveNoticeBillGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, PurchaseReceiveNoticeBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(PurchaseReceiveNoticeBillGoods::getId, Function.identity()));
            // 尺码明细
            List<PurchaseReceiveNoticeBillSize> billSizeList = billSizeMap.get(bill.getId());
            for (PurchaseReceiveNoticeBillSize size : billSizeList) {
                PurchaseReceiveNoticeBillGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                PurchaseReceiveNoticeBillGoodsDetailData detailData = new PurchaseReceiveNoticeBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(billGoods.getId())));
                goodsQueryResultList.add(detailData);

                detailData.setColumnId(size.getId());
                detailData.setBalancePrice(billGoods.getBalancePrice());
                detailData.setTagPrice(billGoods.getTagPrice());
                detailData.setCurrencyPrice(billGoods.getCurrencyPrice());
                detailData.setDiscount(billGoods.getDiscount());
                detailData.setExchangeRate(billGoods.getExchangeRate());
                detailData.setRemark(billGoods.getRemark());
                detailData.setBarcode(barcodeMap.get(size.getSingleCode()));
                detailData.setGoodsCode(goodsMap.get(size.getGoodsId()));
                detailData.setColorCode(colorMap.get(size.getColorId()));
                detailData.setLongName(longMap.get(size.getLongId()));
                detailData.setSize(sizeMap.get(size.getSizeId()));
                detailData.setQuantity(size.getQuantity());
            }

        }
        return queryResults;
    }

    private QueryWrapper processQueryWrapper(PurchaseReceiveNoticeBillQueryContext context) {
        QueryWrapper<PurchaseReceiveNoticeBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(context.getModuleId()), "module_id", context.getModuleId());
        queryWrapper.eq(StrUtil.isNotEmpty(context.getBillNo()), "bill_no", context.getBillNo());
        queryWrapper.eq(context.getBillDate() != null, "bill_date", context.getBillDate());
        queryWrapper.in(CollUtil.isNotEmpty(context.getBusinessTypeIds()), "business_type_id", context.getBusinessTypeIds());
        queryWrapper.in(CollUtil.isNotEmpty(context.getSupplierIds()), "supplier_id", context.getSupplierIds());
        queryWrapper.in(CollUtil.isNotEmpty(context.getToChannelIds()), "to_channel_id", context.getToChannelIds());
        queryWrapper.eq(StrUtil.isNotEmpty(context.getManualId()), "manual_id", context.getManualId());
        queryWrapper.in(CollUtil.isNotEmpty(context.getCurrencyTypeIds()), "currency_type_id", context.getCurrencyTypeIds());
        queryWrapper.like(StrUtil.isNotEmpty(context.getNotes()), "notes", context.getNotes());
        queryWrapper.in(CollUtil.isNotEmpty(context.getStatus()), "status", context.getStatus());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());
        queryWrapper.ge(null != context.getCheckDateStart(), "check_time", context.getCheckDateStart());
        queryWrapper.le(null != context.getCheckDateEnd(), "check_time", context.getCheckDateEnd());

        return queryWrapper;
    }

    private void convertQueryContext(PurchaseReceiveNoticeBillQueryParam param, PurchaseReceiveNoticeBillQueryContext context) {
        context.setModuleId(param.getModuleId());
        context.setBillNo(param.getBillNo());
        context.setBillDate(param.getBillDate());
        context.setManualId(param.getManualId());
        context.setNotes(param.getNotes());
        context.setStatus(param.getStatus());
        context.setPageNo(param.getPageNo());
        context.setPageSize(param.getPageSize());

        if (CollUtil.isNotEmpty(param.getBusinessType())) {
            context.setBusinessTypeIds(CollUtil.map(businessTypeDao.selectList(new QueryWrapper<BusinessType>().in("name", param.getBusinessType())), BusinessType::getId, true));
        }
        if (CollUtil.isNotEmpty(param.getSupplierCode())) {
            context.setSupplierIds(CollUtil.map(supplierDao.selectList(new QueryWrapper<Supplier>().in("code", param.getSupplierCode())), Supplier::getId, true));
        }
        if (CollUtil.isNotEmpty(param.getToChannelCode())) {
            context.setToChannelIds(CollUtil.map(channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getToChannelCode())), Channel::getId, true));
        }
        if (CollUtil.isNotEmpty(param.getCurrencyType())) {
            context.setCurrencyTypeIds(CollUtil.map(currencyTypeDao.selectList(new QueryWrapper<CurrencyType>().in("name", param.getCurrencyType())), CurrencyType::getId, true));
        }

    }

    @Override
    @Transactional
    public DataResponse save(PurchaseReceiveNoticeBillSaveParam param) {
        PurchaseReceiveNoticeBillSaveContext context = new PurchaseReceiveNoticeBillSaveContext();
        List<String> messageList = new ArrayList<>();
        convertSaveContext(context, param, messageList);
        if (!messageList.isEmpty()) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList.stream().distinct().collect(Collectors.toList()))}));
        }
        purchaseReceiveNoticeBillDao.insert(context.getBill());
        context.getBillGoodsList().forEach(purchaseReceiveNoticeBillGoodsDao::insert);
        context.getBillSizeList().forEach(purchaseReceiveNoticeBillSizeDao::insert);
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_NOTICE_BILL, context.getBill().getId(), context.getBillCustomizeDataDtos());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_NOTICE_BILL_GOODS, context.getGoodsCustomizeData());

        // todo 来货超差

        //todo 核算设置
        return DataResponse.success();
    }

    private void convertSaveContext(PurchaseReceiveNoticeBillSaveContext context, PurchaseReceiveNoticeBillSaveParam param, List<String> messageList) {
        PurchaseReceiveNoticeBill bill = new PurchaseReceiveNoticeBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        bill.setBillNo(systemCommonService.getBillNo(param.getModuleId()));
        bill.setStatus(CheckEnum.NOCHECK.getStatus());
        bill.setProcessStatus(0);
        bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        bill.setSupplierId(baseDbDao.getLongDataBySql(String.format("select id from rbp_supplier where status = 1 and code = '%s'", param.getSupplierCode())));
        bill.setToChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where code = '%s'", param.getToChannelCode())));
        if (StrUtil.isNotEmpty(param.getPurchaseNo())) {
            bill.setPurchaseId(baseDbDao.getLongDataBySql(String.format("select id from rbp_purchase_bill where bill_no  = '%s'", param.getPurchaseNo())));
        }

        if (StrUtil.isNotEmpty(param.getPurchaseNo())){
            PurchaseBill purchaseBill = purchaseBillDao.selectOne(new QueryWrapper<PurchaseBill>().eq("bill_no", param.getPurchaseNo()));
            if (purchaseBill!=null){
                context.getBill().setPurchaseId(purchaseBill.getId());
            } else {
                messageList.add("采购单号不存在");
            }
        }

        // 货品明细
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            messageList.add(getNotNullMessage("goodsDetailData"));
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }

        extractedGoodsAndSizes(context, param, messageList, bill);

        context.setBill(bill);
        context.setBillCustomizeDataDtos(param.getCustomizeData());
    }

    private void extractedGoodsAndSizes(PurchaseReceiveNoticeBillSaveContext context, PurchaseReceiveNoticeBillSaveParam param, List<String> messageList, PurchaseReceiveNoticeBill bill) {
        List<PurchaseReceiveNoticeBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        List<PurchaseReceiveNoticeBillSize> billSizeList = new ArrayList<>();
        context.setBillSizeList(billSizeList);
        // 根据条形码获取货品尺码信息
        Map<String, Barcode> barcodeMap = new HashMap<>();
        // 货品
        Map<String, Long> goodsMap = new HashMap<>();
        // 颜色
        Map<String, Long> colorMap = new HashMap<>();
        // 内长
        Map<String, Long> longMap = new HashMap<>();
        // 尺码
        Map<String, Long> sizeMap = new HashMap<>();
        if (param.getGoodsDetailData().stream().filter(f -> StringUtil.isNotEmpty(f.getBarcode())).count() > 0) {
            List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getBarcode())));
            barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity(), (x1, x2) -> x1));
        } else {
            List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getGoodsCode())));
            goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId, (x1, x2) -> x1));
            List<Color> colorList = colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getColorCode())));
            colorMap = colorList.stream().collect(Collectors.toMap(Color::getCode, Color::getId, (x1, x2) -> x1));
            List<LongInfo> longList = longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getLongName())));
            longMap = longList.stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId, (x1, x2) -> x1));
            List<SizeDetail> sizeClassList = baseDbDao.getSizeNameList(StreamUtil.toList(goodsList, Goods::getId), StreamUtil.toList(param.getGoodsDetailData(), v -> v.getSize()));
            sizeMap = sizeClassList.stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));
        }
        // 尺码明细
        List<PurchaseReceiveNoticeBillSize> sizeList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (PurchaseReceiveNoticeBillGoodsDetailData item : param.getGoodsDetailData()) {
            atomicInteger.getAndIncrement();
            PurchaseReceiveNoticeBillSize size = new PurchaseReceiveNoticeBillSize();
            sizeList.add(size);

            size.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            size.setBillId(bill.getId());
            size.setQuantity(item.getQuantity());

            Barcode barcode = barcodeMap.get(item.getBarcode());
            if (null != barcode) {
                size.setGoodsId(barcode.getGoodsId());
                size.setLongId(barcode.getLongId());
                size.setColorId(barcode.getColorId());
                size.setSizeId(barcode.getSizeId());
            } else {
                size.setGoodsId(goodsMap.get(item.getGoodsCode()));
                size.setLongId(longMap.get(item.getLongName()));
                size.setColorId(colorMap.get(item.getColorCode()));
                size.setSizeId(sizeMap.get(size.getGoodsId() + StrUtil.UNDERLINE + item.getSize()));
            }
            if (null == size.getGoodsId()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "goodsCode"));
            }
            if (null == size.getColorId()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "colorCode"));
            }
            if (null == size.getLongId()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "longName"));
            }
            if (null == size.getSizeId()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "size"));
            }
            if (null == size.getQuantity()) {
                messageList.add(getNotNullMessage(atomicInteger.get(), "quantity"));
            }
            if (CollUtil.isEmpty(messageList)) {
                // 设置货品ID
                item.setGoodsId(size.getGoodsId());
                item.setColumnId(size.getId());
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // TODO 不存在结算价的货品
        List<Long> goodsIdList = param.getGoodsDetailData().stream().filter(f -> null == f.getBalancePrice()).map(v -> v.getGoodsId()).distinct().collect(Collectors.toList());
        // TODO 获取分销价格
        atomicInteger.set(0);
        for (PurchaseReceiveNoticeBillGoodsDetailData item : param.getGoodsDetailData()) {
            // TODO 结算价不存在,则通过分销价格获取
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId())) {

            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "balancePrice"));
            }
        }
        // 尺码根据行ID分组
        Map<Long, PurchaseReceiveNoticeBillSize> receiveBillSizeMap = sizeList.stream().collect(Collectors.toMap(PurchaseReceiveNoticeBillSize::getId, Function.identity()));

        atomicInteger.set(0);
        // 根据货品+价格分组，支持同款多价
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
            atomicInteger.getAndIncrement();
            PurchaseReceiveNoticeBillGoods billGoods = new PurchaseReceiveNoticeBillGoods();
            billGoodsList.add(billGoods);

            billGoods.setId(systemCommonService.getId());
            billGoods.setBillId(bill.getId());
            PurchaseReceiveNoticeBillGoodsDetailData detailData = sizes.get(0);
            billGoods.setGoodsId(detailData.getGoodsId());
            billGoods.setTagPrice(detailData.getTagPrice());
            billGoods.setBalancePrice(detailData.getBalancePrice());
            billGoods.setDiscount(detailData.getDiscount());
            billGoods.setCurrencyPrice(detailData.getCurrencyPrice());
            billGoods.setExchangeRate(detailData.getExchangeRate());
            billGoods.setQuantity(detailData.getQuantity());
            billGoods.setRemark(detailData.getRemark());
            context.addGoodsDetailCustomData(sizes.get(0).getGoodsCustomizeData(), billGoods.getId());
            // 尺码明细
            for (PurchaseReceiveNoticeBillGoodsDetailData item : sizes) {
                PurchaseReceiveNoticeBillSize billSize = receiveBillSizeMap.get(item.getColumnId());
                billSizeList.add(billSize);
                billSize.setBillGoodsId(billGoods.getId());
            }
        });

    }

    private static String getNotNullMessage(String key) {
        return getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotExistMessage(String key) {
        return getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotNullMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotNullMessage(key)});
    }

    private static String getNotExistMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotExistMessage(key)});
    }

    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }

}
