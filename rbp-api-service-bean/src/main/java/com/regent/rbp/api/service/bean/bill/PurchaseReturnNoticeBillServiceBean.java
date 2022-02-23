package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillSize;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.purchaseReturnNoticeBill.PurchaseReturnNoticeBillDao;
import com.regent.rbp.api.dao.purchaseReturnNoticeBill.PurchaseReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dao.purchaseReturnNoticeBill.PurchaseReturnNoticeBillSizeDao;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.GoodsDetailIdentifier;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchaseReturn.PurchaseReturnNoticeBillService;
import com.regent.rbp.api.service.purchaseReturn.context.PurchaseReturnNoticeBillQueryContext;
import com.regent.rbp.api.service.purchaseReturn.context.PurchaseReturnNoticeBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知 Bean
 * @author: HaiFeng
 * @create: 2021/12/30 14:27
 */
@Service
public class PurchaseReturnNoticeBillServiceBean extends ServiceImpl<PurchaseReturnNoticeBillDao, PurchaseReturnNoticeBill> implements PurchaseReturnNoticeBillService {

    @Autowired
    BaseDbDao baseDbDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    SupplierDao supplierDao;
    @Autowired
    CurrencyTypeDao currencyTypeDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    LongDao longDao;
    @Autowired
    SizeDetailDao sizeDetailDao;
    @Autowired
    BarcodeDao barcodeDao;
    @Autowired
    PurchaseReturnNoticeBillDao purchaseReturnNoticeBillDao;
    @Autowired
    PurchaseReturnNoticeBillGoodsDao purchaseReturnNoticeBillGoodsDao;
    @Autowired
    PurchaseReturnNoticeBillSizeDao purchaseReturnNoticeBillSizeDao;
    @Autowired
    PurchaseBillDao purchaseBillDao;

    @Autowired
    DbService dbService;
    @Autowired
    BaseDbService baseDbService;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private Validator validator;


    @Override
    public PageDataResponse<PurchaseReturnNoticeBillQueryResult> query(PurchaseReturnNoticeBillQueryParam param) {
        PurchaseReturnNoticeBillQueryContext context = new PurchaseReturnNoticeBillQueryContext();
        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);
        //查询数据
        PageDataResponse<PurchaseReturnNoticeBillQueryResult> result = new PageDataResponse<>();
        Page<PurchaseReturnNoticeBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);
        queryWrapper.orderByDesc("updated_time");
        IPage<PurchaseReturnNoticeBill> salesPageData = purchaseReturnNoticeBillDao.selectPage(pageModel, queryWrapper);
        List<PurchaseReturnNoticeBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

        result.setTotalCount(salesPageData.getTotal());
        result.setData(list);
        return result;
    }

    /**
     * 将查询参数转换成 查询的上下文
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(PurchaseReturnNoticeBillQueryParam param, PurchaseReturnNoticeBillQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, PurchaseReturnNoticeBillQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, PurchaseReturnNoticeBillQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setModuleId(param.getModuleId());
        context.setBillNo(param.getBillNo());

        /******** 日期 ********/
        if (null != param.getBillDate()) {
            context.setBillDate(DateUtil.getDate(param.getBillDate(), DateUtil.SHORT_DATE_FORMAT));
        }
        // 创建日期
        if (null != param.getCreatedDateStart()) {
            context.setCreatedDateStart(DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT));
        }
        if (null != param.getCreatedDateEnd()) {
            context.setCreatedDateEnd(DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT));
        }
        // 审核日期
        if (null != param.getCheckDateStart()) {
            context.setCheckDateStart(DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT));
        }
        if (null != param.getCheckDateEnd()) {
            context.setCheckDateEnd(DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT));
        }
        // 修改日期
        if (null != param.getUpdatedDateStart()) {
            context.setUpdatedDateStart(DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT));
        }
        if (null != param.getUpdatedDateEnd()) {
            context.setUpdatedDateEnd(DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT));
        }

        // 业务类型
        if (param.getBusinessType() != null && param.getBusinessType().length > 0) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_business_type where status=100 %s", AppendSqlUtil.getInStrSql("name", Arrays.asList(param.getBusinessType()))));
            if (CollUtil.isNotEmpty(idList)) {
                context.setBusinessTypeIds(idList.stream().mapToLong(Long::longValue).toArray());
            }
        }
        // 供应商
        if (param.getSupplierCode() != null && param.getSupplierCode().length > 0) {
            List<Supplier> supplierList = supplierDao.selectList(new LambdaQueryWrapper<Supplier>().in(Supplier::getCode, param.getSupplierCode()));
            if (CollUtil.isNotEmpty(supplierList)) {
                context.setSupplierCodeIds(supplierList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
        // 收货渠道
        if (param.getToChannelCode() != null && param.getToChannelCode().length > 0) {
            List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getToChannelCode()));
            if (CollUtil.isNotEmpty(channelList)) {
                context.setToChannelCodeIds(channelList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
        // 币种类型
        if (param.getCurrencyType() != null && param.getCurrencyType().length > 0) {
            List<CurrencyType> currencyTypeList = currencyTypeDao.selectList(new LambdaQueryWrapper<CurrencyType>().in(CurrencyType::getName, param.getCurrencyType()));
            if (CollUtil.isNotEmpty(currencyTypeList)) {
                context.setCurrencyTypeIds(currencyTypeList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
    }

    /**
     * 整理查询条件构造器
     *
     * @param context
     * @return
     */
    private QueryWrapper processQueryWrapper(PurchaseReturnNoticeBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq(StringUtil.isNotEmpty(context.getModuleId()), "module_id", context.getModuleId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getBillNo()), "bill_no", context.getBillNo());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getManualId()), "manual_id", context.getManualId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getNotes()), "notes", context.getNotes());

        queryWrapper.eq(null != context.getBillDate(), "bill_date", context.getBillDate());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());
        queryWrapper.ge(null != context.getCheckDateStart(), "check_time", context.getCheckDateStart());
        queryWrapper.le(null != context.getCheckDateEnd(), "check_time", context.getCheckDateEnd());

        if (context.getBusinessTypeIds() != null && context.getBusinessTypeIds().length > 0) {
            queryWrapper.in("business_type_id", context.getBusinessTypeIds());
        }
        if (context.getSupplierCodeIds() != null && context.getSupplierCodeIds().length > 0) {
            queryWrapper.in("supplier_id", context.getSupplierCodeIds());
        }
        if (context.getToChannelCodeIds() != null && context.getToChannelCodeIds().length > 0) {
            queryWrapper.in("channel_id", context.getToChannelCodeIds());
        }
        if (context.getCurrencyTypeIds() != null && context.getCurrencyTypeIds().length > 0) {
            queryWrapper.in("currency_type_id", context.getCurrencyTypeIds());
        }
        if (context.getStatusIds() != null && context.getStatusIds().length > 0) {
            queryWrapper.in("status", context.getStatusIds());
        }

        return queryWrapper;
    }

    /**
     * 处理查询结果的属性
     *
     * @param list
     * @return
     */
    public List<PurchaseReturnNoticeBillQueryResult> convertQueryResult(List<PurchaseReturnNoticeBill> list) {
        List<PurchaseReturnNoticeBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(PurchaseReturnNoticeBill::getId).distinct().collect(Collectors.toList());
        // 供应商
        Set<Long> supplierIds = StreamUtil.toSet(list, PurchaseReturnNoticeBill::getSupplierId);
        Map<Long, IdNameCodeDto> supplierMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Supplier>().in("id", supplierIds), Supplier.class, LanguageTableEnum.SUPPLIER);
        // 退货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, PurchaseReturnNoticeBill::getChannelId);
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, PurchaseReturnNoticeBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, PurchaseReturnNoticeBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<PurchaseReturnNoticeBillSize> purchaseBillSizeList = purchaseReturnNoticeBillSizeDao.selectList(new LambdaQueryWrapper<PurchaseReturnNoticeBillSize>().in(PurchaseReturnNoticeBillSize::getBillId, billIdList));
        List<PurchaseReturnNoticeBillGoods> purchasebillGoodsList = purchaseReturnNoticeBillGoodsDao.selectList(new LambdaQueryWrapper<PurchaseReturnNoticeBillGoods>().in(PurchaseReturnNoticeBillGoods::getBillId, billIdList));
        // 根据单据分组
        Map<Long, List<PurchaseReturnNoticeBillSize>> billSizeMap = purchaseBillSizeList.stream().collect(Collectors.groupingBy(PurchaseReturnNoticeBillSize::getBillId));
        Map<Long, List<PurchaseReturnNoticeBillGoods>> billGoodsMap = purchasebillGoodsList.stream().collect(Collectors.groupingBy(PurchaseReturnNoticeBillGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(purchasebillGoodsList, PurchaseReturnNoticeBillGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseReturnNoticeBillSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseReturnNoticeBillSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseReturnNoticeBillSize::getSizeId)));
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
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(StreamUtil.toList(list, PurchaseReturnNoticeBill::getModuleId));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.SEND_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.SEND_BILL_GOODS, StreamUtil.toList(purchasebillGoodsList, PurchaseReturnNoticeBillGoods::getId));
        // 填充
        for (PurchaseReturnNoticeBill bill : list) {
            PurchaseReturnNoticeBillQueryResult queryResult = new PurchaseReturnNoticeBillQueryResult();
            queryResults.add(queryResult);

            queryResult.setModuleId(bill.getModuleId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillDate(bill.getBillDate());
            queryResult.setBusinessType(OptionalUtil.ofNullable(businessTypeMap.get(bill.getBusinessTypeId()), IdNameDto::getName));
            queryResult.setSupplierCode(OptionalUtil.ofNullable(supplierMap.get(bill.getSupplierId()), IdNameCodeDto::getCode));
            queryResult.setToChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getChannelId()), IdNameCodeDto::getCode));
            queryResult.setCurrencyType(OptionalUtil.ofNullable(currencyTypeMap.get(bill.getCurrencyTypeId()), IdNameDto::getName));
            queryResult.setTaxRate(bill.getTaxRate());
            queryResult.setNotes(bill.getNotes());
            queryResult.setStatus(bill.getStatus());
            queryResult.setCreatedTime(bill.getCreatedTime());
            queryResult.setUpdatedTime(bill.getUpdatedTime());
            queryResult.setCheckTime(bill.getCheckTime());

            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(bill.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            queryResult.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(bill.getId())));

            // 货品列表
            List<PurchaseReturnNoticeBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<PurchaseReturnNoticeBillGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, PurchaseReturnNoticeBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(PurchaseReturnNoticeBillGoods::getId, Function.identity()));
            // 尺码明细
            List<PurchaseReturnNoticeBillSize> billSizeList = billSizeMap.get(bill.getId());
            for (PurchaseReturnNoticeBillSize size : billSizeList) {
                PurchaseReturnNoticeBillGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                PurchaseReturnNoticeBillGoodsDetailData detailData = new PurchaseReturnNoticeBillGoodsDetailData();
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
        return queryResults;
    }

    @Transactional
    @Override
    public DataResponse save(PurchaseReturnNoticeBillSaveParam param) {
        PurchaseReturnNoticeBillSaveContext context = new PurchaseReturnNoticeBillSaveContext(param);
        // 验证属性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        // 写入单据
        this.save(context);
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_NOTICE_BILL, context.getBill().getId(), context.getBillCustomizeDataDtos());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.PURCHASE_RECEIVE_NOTICE_BILL_GOODS, context.getGoodsCustomizeData());
        return DataResponse.success();
    }

    /**
     * 验证属性
     *
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(PurchaseReturnNoticeBillSaveParam param, PurchaseReturnNoticeBillSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        PurchaseReturnNoticeBill bill = context.getBill();
        List<PurchaseReturnNoticeBillGoods> billGoodsList = context.getBillGoodsList();
        List<PurchaseReturnNoticeBillSize> billSizeList = context.getBillSizeList();

        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setBillDate(param.getBillDate());

        Long businessTypeId = baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType()));
        bill.setBusinessTypeId(businessTypeId);

        Supplier supplier = supplierDao.selectOne(new LambdaQueryWrapper<Supplier>().eq(Supplier::getCode, param.getSupplierCode()));
        bill.setSupplierId(supplier.getId());

        Channel channel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
        bill.setChannelId(channel.getId());

        // 采购单
        if (StringUtil.isNotEmpty(param.getPurchaseNo())) {
            PurchaseBill purchaseBill = purchaseBillDao.selectOne(new LambdaQueryWrapper<PurchaseBill>().eq(BillMasterData::getBillNo, param.getPurchaseNo()));
            bill.setPurchaseId(purchaseBill.getId());
        }

        extractedGoodsAndSizes(context, param, errorMsgList);
        context.setBillCustomizeDataDtos(param.getCustomizeData());
        return errorMsgList;
    }

    private void extractedGoodsAndSizes(PurchaseReturnNoticeBillSaveContext context, PurchaseReturnNoticeBillSaveParam param, List<String> messageList) {
        List<PurchaseReturnNoticeBillGoodsDetailData> goodsDetailData = param.getGoodsDetailData();
        List<String> barcodes = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getBarcode()) ? e.getBarcode() : null, true);
        List<String> goodsCode = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getGoodsCode()) ? e.getGoodsCode() : null, true);
        List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsCode));
        List<Long> goodsIdsList = CollUtil.map(goodsList, Goods::getId, true);
        List<String> colorCodes = CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReturnNoticeBillGoodsDetailData::getColorCode, true));
        List<String> longNames = CollUtil.distinct(CollUtil.map(goodsDetailData, PurchaseReturnNoticeBillGoodsDetailData::getLongName, true));
        List<String> sizeNames = CollUtil.map(goodsDetailData, PurchaseReturnNoticeBillGoodsDetailData::getSize, true); // don't distinct, must align

        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        Map<String, Long> goodsMap = goodsIdsList.isEmpty() ? Collections.emptyMap() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorCodes.isEmpty() ? Collections.emptyMap() : colorDao.selectList(new QueryWrapper<Color>().in("code", colorCodes)).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", longNames)).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<Long, Map<String, Long>> goodsSizeNameMap = baseDbDao.getGoodsIdSizeNameIdMap(goodsCode, sizeNames, goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId)));
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
                e.setColorId(colorMap.get(e.getColorCode()));
                e.setLongId(longMap.get(e.getLongName()));
                e.setSizeId(goodsSizeNameMap.getOrDefault(e.getGoodsId(), Collections.emptyMap()).get(e.getSize()));
            }
        });

        if (!ValidateMessageUtil.pass(validator.validate(param, Complex.class), messageList)) return;

        // 根据货品+价格分组，支持同款多价
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(GoodsDetailIdentifier::getSameGoodsDiffPriceKey)).forEach((key, sizes) -> {
            PurchaseReturnNoticeBillGoods billGoods = BeanUtil.copyProperties(sizes.get(0), PurchaseReturnNoticeBillGoods.class);
            context.addBillGoods(billGoods);
            context.addGoodsDetailCustomData(sizes.get(0).getGoodsCustomizeData(), billGoods.getId());
            sizes.forEach(size -> {
                PurchaseReturnNoticeBillSize billSize = BeanUtil.copyProperties(size, PurchaseReturnNoticeBillSize.class);
                billSize.setBillGoodsId(billGoods.getId());
                context.addBillSize(billSize);
            });
        });

    }

    private PurchaseReturnNoticeBillGoods giveBillGoods(Long billId, PurchaseReturnNoticeBillGoodsDetailData goodsResult, Long goodsId) {
        PurchaseReturnNoticeBillGoods billGoods = new PurchaseReturnNoticeBillGoods();
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoods.setBillId(billId);
        billGoods.setGoodsId(goodsId);
        billGoods.setDiscount(goodsResult.getDiscount());
        billGoods.setTagPrice(goodsResult.getTagPrice());
        billGoods.setBalancePrice(goodsResult.getBalancePrice());
        billGoods.setCurrencyPrice(goodsResult.getCurrencyPrice());
        billGoods.setQuantity(goodsResult.getQuantity());
        billGoods.setRemark(goodsResult.getRemark());
        return billGoods;
    }

    private PurchaseReturnNoticeBillSize giveBillSize(Long billId, Long billGoodsId, Barcode barcode, BigDecimal quantity) {
        PurchaseReturnNoticeBillSize billSize = new PurchaseReturnNoticeBillSize();
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSize.setBillId(billId);
        billSize.setBillGoodsId(billGoodsId);
        billSize.setGoodsId(barcode.getGoodsId());
        billSize.setColorId(barcode.getColorId());
        billSize.setLongId(barcode.getLongId());
        billSize.setSizeId(barcode.getSizeId());
        billSize.setQuantity(quantity);
        return billSize;
    }

    private void save(PurchaseReturnNoticeBillSaveContext context) {
        purchaseReturnNoticeBillDao.insert(context.getBill());
        for (PurchaseReturnNoticeBillGoods goods : context.getBillGoodsList()) {
            purchaseReturnNoticeBillGoodsDao.insert(goods);
        }
        for (PurchaseReturnNoticeBillSize size : context.getBillSizeList()) {
            purchaseReturnNoticeBillSizeDao.insert(size);
        }
    }

}
