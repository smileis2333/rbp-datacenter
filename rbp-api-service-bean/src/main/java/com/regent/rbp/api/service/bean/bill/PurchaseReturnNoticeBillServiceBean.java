package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.coupon.RetailPayType;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBill;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillGoods;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnNoticeBillSize;
import com.regent.rbp.api.core.salesOrder.SalesOrderBill;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillGoods;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillPayment;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillSize;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.core.sendBill.SendBillSize;
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
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;
import com.regent.rbp.api.dto.sale.SaleOrderSaveParam;
import com.regent.rbp.api.dto.sale.SalesOrderBillGoodsResult;
import com.regent.rbp.api.dto.sale.SalesOrderBillPaymentResult;
import com.regent.rbp.api.dto.send.SendBillGoodsDetailData;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchaseReturn.PurchaseReturnNoticeBillService;
import com.regent.rbp.api.service.purchaseReturn.context.PurchaseReturnNoticeBillQueryContext;
import com.regent.rbp.api.service.purchaseReturn.context.PurchaseReturnNoticeBillSaveContext;
import com.regent.rbp.api.service.sale.context.SalesOrderBillSaveContext;
import com.regent.rbp.api.service.send.context.SendBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if(context.getBusinessTypeIds() != null && context.getBusinessTypeIds().length > 0) {
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
        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        // 写入单据
        this.save(context);
        return DataResponse.success();
    }

    /**
     * 验证属性
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(PurchaseReturnNoticeBillSaveParam param, PurchaseReturnNoticeBillSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        PurchaseReturnNoticeBill bill = context.getBill();
        List<PurchaseReturnNoticeBillGoods> billGoodsList = context.getBillGoodsList();
        List<PurchaseReturnNoticeBillSize> billSizeList = context.getBillSizeList();

        if (StringUtil.isEmpty(param.getModuleId())) {
            errorMsgList.add("模块编号(moduleId)不能为空");
        }
        if (StringUtil.isEmpty(param.getManualId())) {
            errorMsgList.add("模块编号(moduleId)不能为空");
        } else {
            bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        }
        if (StringUtil.isEmpty(param.getBillDate())) {
            errorMsgList.add("单据日期(billDate)不能为空");
        } else {
            bill.setBillDate(DateUtil.getDate(param.getBillDate(), DateUtil.SHORT_DATE_FORMAT));
        }
        // 业务类型
        if (StringUtil.isEmpty(param.getBusinessType())) {
            errorMsgList.add("业务类型名称(businessType)不能为空");
        } else {
            Long businessTypeId = baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType()));
            if (businessTypeId == null) {
                errorMsgList.add("业务类型名称(businessType)不存在");
            } else {
                bill.setBusinessTypeId(businessTypeId);
            }
        }
        // 供应商
        if (StringUtil.isEmpty(param.getSupplierCode())) {
            errorMsgList.add("供应商编码(supplierCode)不能为空");
        } else {
            Supplier supplier = supplierDao.selectOne(new LambdaQueryWrapper<Supplier>().eq(Supplier::getCode, param.getSupplierCode()));
            if (supplier == null) {
                errorMsgList.add("供应商编码(supplierCode)不存在");
            } else {
                bill.setSupplierId(supplier.getId());
            }
        }
        // 退货渠道
        if (StringUtil.isEmpty(param.getChannelCode())) {
            errorMsgList.add("退货渠道编号(channelCode)不能为空");
        } else {
            Channel channel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
            if (channel == null) {
                errorMsgList.add("退货渠道编号(channelCode)不存在");
            } else {
                bill.setChannelId(channel.getId());
            }
        }
        // 采购单
        if (StringUtil.isNotEmpty(param.getPurchaseNo())) {
            PurchaseBill purchaseBill = purchaseBillDao.selectOne(new LambdaQueryWrapper<PurchaseBill>().eq(BillMasterData::getBillNo, param.getPurchaseNo()));
            if (purchaseBill == null) {
                errorMsgList.add("采购单号(purchaseNo)不存在");
            } else {
                bill.setPurchaseId(purchaseBill.getId());
            }
        }
        if (param.getStatus() == null) {
            errorMsgList.add("单据状态(status)不能为空");
        } else {
            List<Integer> statusList = new ArrayList<>();
            statusList.add(0);
            statusList.add(1);
            statusList.add(2);
            statusList.add(3);
            if (statusList.stream().filter(f -> f.equals(param.getStatus())).count() == 0) {
                errorMsgList.add("单据状态(status)内容必须是 (0.未审核,1.已审核,2.反审核,3.已作废)");
            }
        }

        if (param.getGoodsDetailData() == null || param.getGoodsDetailData().size() == 0) {
            errorMsgList.add("货品明细(goodsDetailData)不能为空");
        } else {
            if (StringUtils.isBlank(param.getGoodsDetailData().get(0).getBarcode())) {
                // 货品+颜色+内长+尺码
                List<String> goodsNos = param.getGoodsDetailData().stream().map(PurchaseReturnNoticeBillGoodsDetailData::getGoodsCode).distinct().collect(Collectors.toList());
                List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getCode, goodsNos));
                Map<String, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getCode, t->t));

                List<String> colorNos = param.getGoodsDetailData().stream().map(PurchaseReturnNoticeBillGoodsDetailData::getColorCode).distinct().collect(Collectors.toList());
                List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getCode, colorNos));
                Map<String, Color> colorMap = colorList.stream().collect(Collectors.toMap(Color::getCode, t->t));

                List<String> longNos = param.getGoodsDetailData().stream().map(PurchaseReturnNoticeBillGoodsDetailData::getLongName).distinct().collect(Collectors.toList());
                List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getName, longNos));
                Map<String, LongInfo> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getName, t->t));

                Map<String, Long> sizeMap = new HashMap<>();

                for (PurchaseReturnNoticeBillGoodsDetailData goodsResult : param.getGoodsDetailData()) {
                    Barcode barcode = new Barcode();
                    Goods goods = null;
                    // 货品
                    if (!goodsMap.containsKey(goodsResult.getGoodsCode())) {
                        errorMsgList.add(String.format("货号(goodsCode) %s 不存在", goodsResult.getGoodsCode()));
                    } else {
                        goods = goodsMap.get(goodsResult.getGoodsCode());
                        barcode.setGoodsId(goods.getId());
                    }
                    // 颜色
                    if (!colorMap.containsKey(goodsResult.getColorCode())) {
                        errorMsgList.add(String.format("颜色编号(colorCode) %s 不存在", goodsResult.getColorCode()));
                    } else {
                        barcode.setColorId(colorMap.get(goodsResult.getColorCode()).getId());
                    }
                    // 内长
                    if (!longMap.containsKey(goodsResult.getLongName())) {
                        errorMsgList.add(String.format("内长(longName) %s 不存在", goodsResult.getLongName()));
                    } else {
                        barcode.setLongId(longMap.get(goodsResult.getLongName()).getId());
                    }
                    // 尺码
                    String key = goods.getCode() + "-" + goodsResult.getSize();
                    if (!sizeMap.containsKey(key)) {
                        SizeDetail sizeDetail = sizeDetailDao.selectOne(new LambdaQueryWrapper<SizeDetail>().eq(SizeDetail::getSizeClassId, goods.getSizeClassId()).eq(SizeDetail::getName, goodsResult.getSize()));
                        if (sizeDetail == null) {
                            errorMsgList.add(String.format("尺码(size) %s 不存在", goodsResult.getSize()));
                        } else {
                            sizeMap.put(key, sizeDetail.getId());
                            barcode.setSizeId(sizeDetail.getId());
                        }
                    } else {
                        barcode.setSizeId(sizeMap.get(key));
                    }

                    PurchaseReturnNoticeBillGoods billGoods = this.giveBillGoods(bill.getId(), goodsResult, barcode.getGoodsId());
                    billGoodsList.add(billGoods);

                    PurchaseReturnNoticeBillSize billSize = this.giveBillSize(bill.getId(), billGoods.getId(), barcode, goodsResult.getQuantity());
                    billSizeList.add(billSize);
                }
            } else {
                // 条码
                List<String> barcodes =  param.getGoodsDetailData().stream().map(PurchaseReturnNoticeBillGoodsDetailData::getBarcode).distinct().collect(Collectors.toList());
                List<Barcode> barcodeList = barcodeDao.selectList(new LambdaQueryWrapper<Barcode>().in(Barcode::getBarcode, barcodes));
                Map<String, Barcode> barcodeMap = barcodeList.stream().collect(Collectors.toMap(Barcode::getBarcode, t -> t));
                for (PurchaseReturnNoticeBillGoodsDetailData goodsResult : param.getGoodsDetailData()) {
                    if (!barcodeMap.containsKey(goodsResult.getBarcode())) {
                        errorMsgList.add(String.format("条形码(barcode) %s 不存在", goodsResult.getBarcode()));
                    } else {
                        Barcode barcode = barcodeMap.get(goodsResult.getBarcode());

                        PurchaseReturnNoticeBillGoods billGoods = this.giveBillGoods(bill.getId(), goodsResult, barcode.getGoodsId());
                        billGoodsList.add(billGoods);

                        PurchaseReturnNoticeBillSize billSize = this.giveBillSize(bill.getId(), billGoods.getId(), barcode, goodsResult.getQuantity());
                        billSizeList.add(billSize);;
                    }
                }
            }
        }
        return errorMsgList;
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
