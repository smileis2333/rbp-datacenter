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
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBill;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBillGoods;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBillSize;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.purchaseReceiveBill.PurchaseReceiveBillDao;
import com.regent.rbp.api.dao.purchaseReceiveBill.PurchaseReceiveBillGoodsDao;
import com.regent.rbp.api.dao.purchaseReceiveBill.PurchaseReceiveBillSizeDao;
import com.regent.rbp.api.dao.salePlan.BusinessTypeDao;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.*;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchase.PurchaseReceiveBillService;
import com.regent.rbp.api.service.purchase.context.PurchaseReceiveBillQueryContext;
import com.regent.rbp.api.service.purchase.context.PurchaseReceiveBillSaveContext;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.common.model.basic.dto.BalanceDetailSampleDto;
import com.regent.rbp.common.model.basic.dto.BaseGoodsSizeDto;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.common.service.stock.StockDetailService;
import com.regent.rbp.common.service.stock.UsableStockDetailService;
import com.regent.rbp.common.utils.ValidateReceiveDifferenceTool;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
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
 * @date : 2021/12/30
 * @description
 */
@Service
public class PurchaseReceiveBillServiceBean implements PurchaseReceiveBillService {
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private Validator validator;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private PurchaseReceiveBillDao purchaseReceiveBillDao;
    @Autowired
    private PurchaseReceiveBillGoodsDao purchaseReceiveBillGoodsDao;
    @Autowired
    private PurchaseReceiveBillSizeDao purchaseReceiveBillSizeDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private ValidateReceiveDifferenceTool validateReceiveDifferenceTool;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private StockDetailService stockDetailService;
    @Autowired
    private UsableStockDetailService usableStockDetailService;
    @Autowired
    private BusinessTypeDao businessTypeDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private CurrencyTypeDao currencyTypeDao;
    @Autowired
    private DbService dbService;
    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Override
    public PageDataResponse<PurchaseReceiveBillQueryResult> query(PurchaseReceiveBillQueryParam param) {
        PurchaseReceiveBillQueryContext context = new PurchaseReceiveBillQueryContext();
        convertQueryContext(param, context);
        Page<PurchaseReceiveBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = processQueryWrapper(context);
        IPage<PurchaseReceiveBill> receiveBillIPage = purchaseReceiveBillDao.selectPage(pageModel, queryWrapper);
        List<PurchaseReceiveBillQueryResult> list = convertQueryResult(receiveBillIPage.getRecords());
        PageDataResponse<PurchaseReceiveBillQueryResult> result = new PageDataResponse<>();
        result.setTotalCount(receiveBillIPage.getTotal());
        result.setData(list);
        return result;
    }

    private List<PurchaseReceiveBillQueryResult> convertQueryResult(List<PurchaseReceiveBill> list) {
        List<PurchaseReceiveBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(PurchaseReceiveBill::getId).distinct().collect(Collectors.toList());
        // 收货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, PurchaseReceiveBill::getToChannelId);
        Set<Long> supplierIds = StreamUtil.toSet(list, PurchaseReceiveBill::getSupplierId);
        channelIds.addAll(StreamUtil.toSet(list, PurchaseReceiveBill::getToChannelId));
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        Map<Long, IdNameCodeDto> supplierMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Supplier>().in("id", supplierIds), Supplier.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, PurchaseReceiveBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, PurchaseReceiveBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<PurchaseReceiveBillSize> billSizes = purchaseReceiveBillSizeDao.selectList(new LambdaQueryWrapper<PurchaseReceiveBillSize>().in(PurchaseReceiveBillSize::getBillId, billIdList));
        List<PurchaseReceiveBillGoods> billGoodss = purchaseReceiveBillGoodsDao.selectList(new LambdaQueryWrapper<PurchaseReceiveBillGoods>().in(PurchaseReceiveBillGoods::getBillId, billIdList));
        // 根据单据分组
        Map<Long, List<PurchaseReceiveBillSize>> billSizeMap = billSizes.stream().collect(Collectors.groupingBy(PurchaseReceiveBillSize::getBillId));
        Map<Long, List<PurchaseReceiveBillGoods>> billGoodsMap = billGoodss.stream().collect(Collectors.groupingBy(PurchaseReceiveBillGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(billGoodss, PurchaseReceiveBillGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = billSizes.isEmpty() ? Collections.emptyList() : colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(billSizes, PurchaseReceiveBillSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = billSizes.isEmpty() ? Collections.emptyList() : longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(billSizes, PurchaseReceiveBillSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = billSizes.isEmpty() ? Collections.emptyList() : sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(billSizes, PurchaseReceiveBillSize::getSizeId)));
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
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(list, PurchaseReceiveBill::getModuleId, true));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RECEIVE_NOTICE_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_RECEIVE_NOTICE_BILL_GOODS, CollUtil.map(billGoodss, PurchaseReceiveBillGoods::getId, true));
        // 填充
        for (PurchaseReceiveBill bill : list) {
            PurchaseReceiveBillQueryResult queryResult = new PurchaseReceiveBillQueryResult();
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
            List<PurchaseReceiveBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<PurchaseReceiveBillGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, PurchaseReceiveBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(PurchaseReceiveBillGoods::getId, Function.identity()));
            // 尺码明细
            List<PurchaseReceiveBillSize> billSizeList = billSizeMap.get(bill.getId());
            if (CollUtil.isNotEmpty(billSizeList)) {
                for (PurchaseReceiveBillSize size : billSizeList) {
                    PurchaseReceiveBillGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                    PurchaseReceiveBillGoodsDetailData detailData = new PurchaseReceiveBillGoodsDetailData();
                    // 货品自定义字段，格式化单选类型字段
                    detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(billGoods.getId())));
                    goodsQueryResultList.add(detailData);

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

        }
        return queryResults;
    }

    private QueryWrapper processQueryWrapper(PurchaseReceiveBillQueryContext context) {
        QueryWrapper<PurchaseReceiveBill> queryWrapper = new QueryWrapper<>();
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

    private void convertQueryContext(PurchaseReceiveBillQueryParam param, PurchaseReceiveBillQueryContext context) {
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
    public DataResponse save(PurchaseReceiveBillSaveParam param) {
        PurchaseReceiveBillSaveContext context = new PurchaseReceiveBillSaveContext();
        List<String> messageList = new ArrayList<>();
        convertSaveContext(context, param, messageList);
        if (!messageList.isEmpty()) {
            return ModelDataResponse.errorParameter(messageList.stream().collect(Collectors.joining(",")));
        }
        purchaseReceiveBillDao.insert(context.getBill());
        context.getBillGoodsList().forEach(purchaseReceiveBillGoodsDao::insert);
        context.getBillSizeList().forEach(purchaseReceiveBillSizeDao::insert);
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_BILL, context.getBill().getId(), context.getBillCustomizeDataDtos());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_BILL_GOODS, context.getGoodsCustomizeData());
        validateReceiveDifferenceTool.checkGoodsReceiveDifferent(null, convertToBaseGoodsSizeDto(context.getBillSizeList()), context.getBill().getPurchaseId(), InformationConstants.ModuleConstants.PURCHASE_RECEIVE_NOTICE_BILL);
        balanceSetting(context);
        updateStock(context);
        return DataResponse.success();
    }

    private void updateStock(PurchaseReceiveBillSaveContext context) {
        Integer status = context.getBill().getStatus();
        if (status == CheckEnum.CHECK.getStatus()) {
            checkModifyStock(context.getBill().getId());
        } else if (status == CheckEnum.UNCHECK.getStatus()) {
            unCheckModifyStock(context.getBill().getId());
        }
    }

    protected <T extends BillGoodsSizeData> List<BaseGoodsSizeDto> convertToBaseGoodsSizeDto(List<T> sourceList) {
        List<BaseGoodsSizeDto> targetList = new ArrayList<>();
        if (CollUtil.isEmpty(sourceList)) {
            return targetList;
        }
        sourceList.forEach(item -> {
            BaseGoodsSizeDto sizeDto = new BaseGoodsSizeDto();
            targetList.add(sizeDto);

            sizeDto.setId(item.getId());
            sizeDto.setBillId(item.getBillId());
            sizeDto.setBillGoodsId(item.getBillGoodsId());
            sizeDto.setGoodsId(item.getGoodsId());
            sizeDto.setColorId(item.getColorId());
            sizeDto.setLongId(item.getLongId());
            sizeDto.setSizeId(item.getSizeId());
            sizeDto.setQuantity(item.getQuantity());
        });
        return targetList;
    }

    private void balanceSetting(PurchaseReceiveBillSaveContext context) {
        Supplier supplier = supplierDao.selectOne(new QueryWrapper<Supplier>().eq("id", context.getBill().getSupplierId()));
        // 单据主体
        BalanceDetailSampleDto sampleDto = new BalanceDetailSampleDto();
        PurchaseReceiveBill bill = context.getBill();
        sampleDto.setModuleId(bill.getModuleId());
        sampleDto.setBillId(bill.getId());
        sampleDto.setBillNo(bill.getBillNo());
        sampleDto.setManualId(bill.getManualId());
        sampleDto.setBillDate(bill.getBillDate());
        sampleDto.setBusinessTypeId(bill.getBusinessTypeId());
        sampleDto.setToChannelId(bill.getToChannelId());
        sampleDto.setNotes(bill.getNotes());
        sampleDto.setFundAccountId(supplier.getFundAccountId());
        sampleDto.setSupplierId(supplier.getId());
        // 核算
        if (StatusEnum.CHECK.getStatus().equals(bill.getStatus())) {
            systemCommonService.insertGeneratePayableBalanceDetail(sampleDto, true);
        }
        // 临时核算
        systemCommonService.insertGeneratePayableBalanceTempDetail(sampleDto, true);

    }

    private void convertSaveContext(PurchaseReceiveBillSaveContext context, PurchaseReceiveBillSaveParam param, List<String> messageList) {
        PurchaseReceiveBill bill = new PurchaseReceiveBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        bill.setBillNo(systemCommonService.getBillNo(param.getModuleId()));
        bill.setProcessStatus(0);
        bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        bill.setSupplierId(baseDbDao.getLongDataBySql(String.format("select id from rbp_supplier where status = 1 and code = '%s'", param.getSupplierCode())));
        bill.setToChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where code = '%s'", param.getToChannelCode())));
        if (StrUtil.isNotEmpty(param.getPurchaseNo())) {
            bill.setPurchaseId(baseDbDao.getLongDataBySql(String.format("select id from rbp_purchase_bill where bill_no  = '%s'", param.getPurchaseNo())));
        }
        context.setBill(bill);
        extractedGoodsAndSizes(context, param, messageList);
        context.setBillCustomizeDataDtos(param.getCustomizeData());
    }

    private void extractedGoodsAndSizes(PurchaseReceiveBillSaveContext context, PurchaseReceiveBillSaveParam param, List<String> messageList) {
        List<PurchaseReceiveBillGoodsDetailData> goodsDetailData = param.getGoodsDetailData();
        List<String> barcodes = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getBarcode()) ? e.getBarcode() : null, true);
        List<String> goodsCode = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getGoodsCode()) ? e.getGoodsCode() : null, true);
        List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsCode));
        List<Long> goodsIdsList = CollUtil.map(goodsList, Goods::getId, true);
        List<String> colorCodes = CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getColorCode, true));
        List<String> longNames = CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getLongName, true));
        List<String> sizeNames = CollUtil.map(goodsDetailData, PurchaseReceiveNoticeBillGoodsDetailData::getSize, true); // don't distinct, must align

        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        Map<String, Long> goodsMap = goodsIdsList.isEmpty() ? Collections.emptyMap() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorCodes.isEmpty() ? Collections.emptyMap() : colorDao.selectList(new QueryWrapper<Color>().in("code", colorCodes)).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", longNames)).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        // todo 矫正，在补一层校验确保完整性
        Map<String, Long> sizeMap = baseDbDao.getSizeNameList(goodsIdsList, sizeNames).stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));

        goodsDetailData.forEach(e -> {
            if (StrUtil.isNotBlank(e.getBarcode())) {
                Barcode barcode = null;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    e.setGoodsId(barcode.getGoodsId());
                    e.setColorId(barcode.getColorId());
                    e.setLongId(barcode.getLongId());
                    e.setSizeId(barcode.getSizeId());
                }
            } else {
                e.setGoodsId(goodsMap.get(e.getGoodsCode()));
                e.setColorId(colorMap.get(e.getColorCode()));
                e.setLongId(longMap.get(e.getLongName()));
                e.setSizeId(sizeMap.get(e.getGoodsId() + StrUtil.UNDERLINE + e.getSize()));
            }
        });

        Set<ConstraintViolation<PurchaseReceiveBillSaveParam>> validate = validator.validate(param, Complex.class);
        validate.forEach(e -> {
            messageList.add(e.getMessage());
        });

        if (!messageList.isEmpty()) {
            return;
        }
        // 根据货品+价格分组，支持同款多价
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
            PurchaseReceiveBillGoods billGoods = BeanUtil.copyProperties(sizes.get(0), PurchaseReceiveBillGoods.class);
            context.addBillGoods(billGoods);
            context.addGoodsDetailCustomData(sizes.get(0).getGoodsCustomizeData(), billGoods.getId());
            sizes.forEach(size -> {
                PurchaseReceiveBillSize billSize = BeanUtil.copyProperties(size, PurchaseReceiveBillSize.class);
                billSize.setBillGoodsId(billGoods.getId());
                context.addBillSize(billSize);
            });
        });
    }

    /**
     * * 采购入库单审核库存处理逻辑：
     * 1.增加收方实际库存
     * 2.增加收方可用库存
     *
     * @param billId
     */
    private void checkModifyStock(Long billId) {
        Long toChannelId = purchaseReceiveBillDao.selectById(billId).getToChannelId();
        List<PurchaseReceiveBillSize> billRealSizeList = purchaseReceiveBillSizeDao.selectList(new QueryWrapper<PurchaseReceiveBillSize>().eq("bill_id", billId));
        //是否允许负库存
        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(toChannelId);
        Set<StockDetail> stockDetailSet = new HashSet<>();
        Set<UsableStockDetail> usableStockDetailSet = new HashSet<>();

        for (PurchaseReceiveBillSize billRealSize : billRealSizeList) {
            //1.增加收方实际库存
            StockDetail stockDetail = new StockDetail();
            BeanUtil.copyProperties(billRealSize, stockDetail);
            stockDetail.setChannelId(toChannelId);
            stockDetail.setReduceQuantity(billRealSize.getQuantity());
            stockDetailSet.add(stockDetail);

            //2.增加收方可用库存
            UsableStockDetail usableStockDetail = new UsableStockDetail();
            BeanUtil.copyProperties(billRealSize, usableStockDetail);
            usableStockDetail.setChannelId(toChannelId);
            usableStockDetail.setReduceQuantity(billRealSize.getQuantity());
            usableStockDetailSet.add(usableStockDetail);
        }
        stockDetailService.writeStockDetail(stockDetailSet, isMustPositive);
        usableStockDetailService.writeUsableStockDetail(usableStockDetailSet, isMustPositive);
    }

    /**
     * * 采购入库单反审核库存处理逻辑：
     * 1.减少收方实际库存
     * 2.减少收方可用库存
     *
     * @param billId
     */
    private void unCheckModifyStock(Long billId) {
        Long toChannelId = purchaseReceiveBillDao.selectById(billId).getToChannelId();
        List<PurchaseReceiveBillSize> billRealSizeList = purchaseReceiveBillSizeDao.selectList(new QueryWrapper<PurchaseReceiveBillSize>().eq("bill_id", billId));
        //是否允许负库存
        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(toChannelId);
        Set<StockDetail> stockDetailSet = new HashSet<>();
        Set<UsableStockDetail> usableStockDetailSet = new HashSet<>();

        for (PurchaseReceiveBillSize billRealSize : billRealSizeList) {
            //1.减少收方实际库存
            StockDetail stockDetail = new StockDetail();
            BeanUtil.copyProperties(billRealSize, stockDetail);
            stockDetail.setChannelId(toChannelId);
            stockDetail.setReduceQuantity(billRealSize.getQuantity().negate());
            stockDetailSet.add(stockDetail);

            //2.减少收方可用库存
            UsableStockDetail usableStockDetail = new UsableStockDetail();
            BeanUtil.copyProperties(billRealSize, usableStockDetail);
            usableStockDetail.setChannelId(toChannelId);
            usableStockDetail.setReduceQuantity(billRealSize.getQuantity().negate());
            usableStockDetailSet.add(usableStockDetail);
        }
        stockDetailService.writeStockDetail(stockDetailSet, isMustPositive);
        usableStockDetailService.writeUsableStockDetail(usableStockDetailSet, isMustPositive);
    }
}
