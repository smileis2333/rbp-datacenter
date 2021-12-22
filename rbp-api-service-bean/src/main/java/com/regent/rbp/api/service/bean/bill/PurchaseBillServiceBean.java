package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.BusinessType;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.CurrencyType;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.ProvideGoodsType;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoodsFinal;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSize;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSizeFinal;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillGoodsFinalDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillSizeFinalDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.base.BaseGoodsPriceDto;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseBillGoodsDetailData;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.purchase.PurchaseBillService;
import com.regent.rbp.api.service.purchase.context.PurchaseBillQueryContext;
import com.regent.rbp.api.service.purchase.context.PurchaseBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/12/21
 * @description 采购单 实现类
 */
@Service
public class PurchaseBillServiceBean extends ServiceImpl<PurchaseBillDao, PurchaseBill> implements PurchaseBillService {

    @Autowired
    private PurchaseBillDao purchaseBillDao;
    @Autowired
    private PurchaseBillSizeFinalDao purchaseBillSizeFinalDao;
    @Autowired
    private PurchaseBillGoodsFinalDao purchaseBillGoodsFinalDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private DbService dbService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<PurchaseBillQueryResult> query(PurchaseBillQueryParam param) {
        PurchaseBillQueryContext context = new PurchaseBillQueryContext();
        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);
        //查询数据
        PageDataResponse<PurchaseBillQueryResult> result = new PageDataResponse<>();
        Page<PurchaseBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);
        queryWrapper.orderByDesc("updated_time");
        IPage<PurchaseBill> salesPageData = purchaseBillDao.selectPage(pageModel, queryWrapper);
        List<PurchaseBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

        result.setTotalCount(salesPageData.getTotal());
        result.setData(list);
        return result;
    }

    /**
     * 创建
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse save(PurchaseBillSaveParam param) {
        PurchaseBillSaveContext context = new PurchaseBillSaveContext();
        // 校验并转换
        List<String> messageList = new ArrayList<>();
        this.convertSaveContext(context, param, messageList);
        if (CollUtil.isNotEmpty(messageList)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList.stream().distinct().collect(Collectors.toList()))}));
        }
        PurchaseBill bill = context.getBill();
        List<PurchaseBillGoods> billGoodsList = context.getBillGoodsList();
        List<PurchaseBillGoodsFinal> billGoodsFinalList = context.getBillGoodsFinalList();
        List<PurchaseBillSize> billSizeList = context.getBillSizeList();
        List<PurchaseBillSizeFinal> billSizeFinalList = context.getBillSizeFinalList();

        // 获取单号
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setProcessStatus(StatusEnum.NONE.getStatus());
        bill.setFlowStatus(StatusEnum.NONE.getStatus());
        // 新增订单
        purchaseBillDao.insert(bill);
        // 单据自定义字段
        baseDbService.saveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.PURCHASE_BILL, bill.getId(), bill.getCustomFieldMap());
        // 货品自定义字段
        List<Map<String, Object>> customFieldMapList = billGoodsList.stream().map(v -> v.getCustomFieldMap()).filter(f -> null != f).collect(Collectors.toList());
        baseDbService.batchSaveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.PURCHASE_BILL_GOODS, customFieldMapList);
        // 批量新增货品明细
        List<PurchaseBillGoods> goodsList = new ArrayList<>();
        int i = 0;
        for (PurchaseBillGoods goods : billGoodsList) {
            i++;
            goodsList.add(goods);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billGoodsList.size()) {
                purchaseBillDao.batchInsertGoodsList(goodsList);
                goodsList.clear();
            }
        }
        // 批量新增调整后货品明细
        // 货品自定义字段
        customFieldMapList = billGoodsFinalList.stream().map(v -> v.getCustomFieldMap()).filter(f -> null != f).collect(Collectors.toList());
        baseDbService.batchSaveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.PURCHASE_BILL_GOODS_FINAL, customFieldMapList);
        List<PurchaseBillGoodsFinal> goodsFinalList = new ArrayList<>();
        i = 0;
        for (PurchaseBillGoodsFinal goodsFinal : billGoodsFinalList) {
            i++;
            goodsFinalList.add(goodsFinal);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billGoodsFinalList.size()) {
                purchaseBillDao.batchInsertGoodsFinalList(goodsFinalList);
                goodsFinalList.clear();
            }
        }
        // 批量新增尺码明细
        List<PurchaseBillSize> sizeList = new ArrayList<>();
        i = 0;
        for (PurchaseBillSize size : billSizeList) {
            i++;
            sizeList.add(size);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billSizeList.size()) {
                purchaseBillDao.batchInsertSizeList(sizeList);
                sizeList.clear();
            }
        }
        // 批量新增调整后尺码明细
        List<PurchaseBillSizeFinal> sizeFinalList = new ArrayList<>();
        i = 0;
        for (PurchaseBillSizeFinal sizeFinal : billSizeFinalList) {
            i++;
            sizeFinalList.add(sizeFinal);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billSizeFinalList.size()) {
                purchaseBillDao.batchInsertSizeFinalList(sizeFinalList);
                sizeFinalList.clear();
            }
        }
        return ModelDataResponse.Success(bill.getBillNo());
    }


    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private void convertSaveContext(PurchaseBillSaveContext context, PurchaseBillSaveParam param, List<String> messageList) {
        /****************   订单主体    ******************/
        PurchaseBill bill = new PurchaseBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        context.setBill(bill);
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());

        // 供货类型
        if (StringUtil.isNotEmpty(param.getProvideGoodsType())) {
            bill.setProvideGoodsTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_provide_goods_type where name = '%s'", param.getProvideGoodsType())));
        }
        // 业务类型
        if (StringUtil.isNotEmpty(param.getBusinessType())) {
            bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        }
        // 币种
        if (StringUtil.isNotEmpty(param.getCurrencyType())) {
            bill.setCurrencyTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_currency_type where status = 100 and name = '%s'", param.getCurrencyType())));
        }
        //  供应商编码
        if (StringUtil.isNotEmpty(param.getSupplierCode())) {
            bill.setSupplierId(baseDbDao.getLongDataBySql(String.format("select id from rbp_supplier where status = 1 and code = '%s'", param.getSupplierCode())));
        }
        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            bill.setCustomFieldMap(customFieldMap);
        }

        // 订单主体校验
        if (null == bill.getBillDate()) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (null == bill.getBusinessTypeId()) {
            messageList.add(getNotNullMessage("businessTypeId"));
        }
        if (StringUtil.isEmpty(bill.getManualId())) {
            messageList.add(getNotNullMessage("manualId"));
        }
        if (null == bill.getSupplierId()) {
            messageList.add(getNotNullMessage("supplierCode"));
        }
        if (null == bill.getStatus()) {
            messageList.add(getNotNullMessage("status"));
        }
        // 货品明细
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            messageList.add(getNotNullMessage("goodsDetailData"));
        }
        // 判断手工单号是否重复
        if (messageList.size() == 0) {
            Integer count = purchaseBillDao.selectCount(new QueryWrapper<PurchaseBill>().eq("manual_id", bill.getManualId()));
            if (null != count && count > 0) {
                messageList.add(getMessageByParams("dataExist", new String[]{LanguageUtil.getMessage("manualId")}));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        /****************   货品明细    ******************/
        List<PurchaseBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        List<PurchaseBillGoodsFinal> billGoodsFinalList = new ArrayList<>();
        context.setBillGoodsFinalList(billGoodsFinalList);
        List<PurchaseBillSize> billSizeList = new ArrayList<>();
        context.setBillSizeList(billSizeList);
        List<PurchaseBillSizeFinal> billSizeFinalList = new ArrayList<>();
        context.setBillSizeFinalList(billSizeFinalList);
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
        if (StringUtil.isNotEmpty(param.getGoodsDetailData().get(0).getBarcode())) {
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
        // 来货超差类型
        Map<String, Integer> receiveDifferentTypeMap = getReceiveDifferentTypeList().stream().collect(Collectors.toMap(IdNameDto::getName, v -> (Integer) v.getId(), (x1, x2) -> x1));
        // 尺码明细
        List<PurchaseBillSize> sizeList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (PurchaseBillGoodsDetailData item : param.getGoodsDetailData()) {
            atomicInteger.getAndIncrement();
            PurchaseBillSize size = new PurchaseBillSize();
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
            if (StringUtil.isNotEmpty(item.getReceiveDifferentType()) && null == receiveDifferentTypeMap.get(item.getReceiveDifferentType())) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "receiveDifferentType"));
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
        // 不存在采购价的货品
        List<Long> goodsIdList = param.getGoodsDetailData().stream().filter(f -> null == f.getBalancePrice()).map(v -> v.getGoodsId()).distinct().collect(Collectors.toList());
        // 获取货品价格
        Map<Long, BaseGoodsPriceDto> goodsPriceDtoMap = baseDbService.getBaseGoodsPriceMapByGoodsIds(goodsIdList);
        atomicInteger.set(0);
        for (PurchaseBillGoodsDetailData item : param.getGoodsDetailData()) {
            // 采购价不存在,则通过货品价格获取
            BaseGoodsPriceDto baseGoodsPriceDto = goodsPriceDtoMap.get(item.getGoodsId());
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId()) && null != baseGoodsPriceDto) {
                item.setBalancePrice(baseGoodsPriceDto.getBalancePrice());
                item.setTagPrice(baseGoodsPriceDto.getTagPrice());
                item.setDiscount(baseGoodsPriceDto.getDiscount());
            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "balancePrice"));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // 尺码根据行ID分组
        Map<Long, PurchaseBillSize> purchaseBillSizeMap = sizeList.stream().collect(Collectors.toMap(v -> v.getId(), Function.identity()));

        atomicInteger.set(0);
        // 根据货品分组
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getGoodsCode())).forEach((key, sizes) -> {
            atomicInteger.getAndIncrement();
            PurchaseBillGoodsDetailData detailData = sizes.get(0);
            PurchaseBillGoods billGoods = PurchaseBillGoods.build();
            billGoodsList.add(billGoods);

            billGoods.setBillId(bill.getId());
            billGoods.setGoodsId(detailData.getGoodsId());
            billGoods.setTagPrice(detailData.getTagPrice());
            billGoods.setBalancePrice(detailData.getBalancePrice());
            billGoods.setDiscount(detailData.getDiscount());
            billGoods.setCurrencyPrice(detailData.getCurrencyPrice());
            billGoods.setExchangeRate(detailData.getExchangeRate());
            billGoods.setQuantity(sizes.stream().map(v -> Optional.ofNullable(v.getQuantity()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add));
            billGoods.setReceiveDifferentType(receiveDifferentTypeMap.get(detailData.getReceiveDifferentType()));
            billGoods.setReceiveDifferentPercent(detailData.getReceiveDifferentPercent());
            billGoods.setRemark(detailData.getRemark());
            // 自定义字段
            if (CollUtil.isNotEmpty(detailData.getGoodsCustomizeData())) {
                Map<String, Object> customFieldMap = new HashMap<>();
                customFieldMap.put("id", billGoods.getId());
                detailData.getGoodsCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
                billGoods.setCustomFieldMap(customFieldMap);
            }
            // 尺码明细
            for (PurchaseBillGoodsDetailData item : sizes) {
                PurchaseBillSize billSize = purchaseBillSizeMap.get(item.getColumnId());
                billSizeList.add(billSize);
                billSize.setBillGoodsId(billGoods.getId());
            }
        });
        // 采购调整明细
        billGoodsList.forEach(item -> {
            PurchaseBillGoodsFinal goodsFinal = new PurchaseBillGoodsFinal();
            BeanUtil.copyProperties(item, goodsFinal);
            goodsFinal.setCustomFieldMap(item.getCustomFieldMap());
            billGoodsFinalList.add(goodsFinal);
        });
        billSizeList.forEach(item -> {
            PurchaseBillSizeFinal sizeFinal = new PurchaseBillSizeFinal();
            BeanUtil.copyProperties(item, sizeFinal);
            billSizeFinalList.add(sizeFinal);
        });
    }

    /**
     * 将查询参数转换成 查询的上下文
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(PurchaseBillQueryParam param, PurchaseBillQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, PurchaseBillQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, PurchaseBillQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setModuleId(param.getModuleId());
        context.setManualId(param.getManualId());
        context.setBillNo(param.getBillNo());
        context.setNotes(param.getNotes());
        context.setStatus(param.getStatus());
        context.setFields(param.getFields());

        // 发货渠道
        if (param.getSupplierCode() != null && param.getSupplierCode().length > 0) {
            List<Supplier> idList = supplierDao.selectList(new LambdaQueryWrapper<Supplier>().in(Supplier::getCode, param.getSupplierCode()));
            if (CollUtil.isNotEmpty(idList)) {
                context.setSupplierIds(idList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
        // 业务类型
        if (param.getBusinessType() != null && param.getBusinessType().length > 0) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_business_type where status=100 %s", AppendSqlUtil.getInStrSql("name", Arrays.asList(param.getBusinessType()))));
            if (CollUtil.isNotEmpty(idList)) {
                context.setBusinessTypeIds(idList.stream().mapToLong(Long::longValue).toArray());
            }
        }
        // 币种类型
        if (param.getCurrencyType() != null && param.getCurrencyType().length > 0) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_currency_type where status=100  %s", AppendSqlUtil.getInStrSql("name", Arrays.asList(param.getCurrencyType()))));
            if (CollUtil.isNotEmpty(idList)) {
                context.setCurrencyTypeIds(idList.stream().mapToLong(Long::longValue).toArray());
            }
        }
    }

    /**
     * 来货超差类型
     * 1.货品;2.货品+颜色;3.货品+颜色+尺码
     *
     * @return
     */
    private List<IdNameDto> getReceiveDifferentTypeList() {
        List<IdNameDto> list = new ArrayList<>();
        list.add(new IdNameDto(1, LanguageUtil.getMessage("receiveDifferentType1")));
        list.add(new IdNameDto(2, LanguageUtil.getMessage("receiveDifferentType2")));
        list.add(new IdNameDto(3, LanguageUtil.getMessage("receiveDifferentType3")));

        return list;
    }


    /**
     * 整理查询条件构造器
     *
     * @param context
     * @return
     */
    private QueryWrapper processQueryWrapper(PurchaseBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<PurchaseBill>();

        queryWrapper.eq(StringUtil.isNotEmpty(context.getModuleId()), "module_id", context.getModuleId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getManualId()), "manual_id", context.getManualId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getBillNo()), "bill_no", context.getBillNo());
        queryWrapper.in(null != context.getStatus() && context.getStatus().length > 0, "status", context.getStatus());
        if (null != context.getBusinessTypeIds() && context.getBusinessTypeIds().length > 0) {
            queryWrapper.in("business_type_id", context.getBusinessTypeIds());
        }
        if (null != context.getCurrencyTypeIds() && context.getCurrencyTypeIds().length > 0) {
            queryWrapper.in("currency_type_id", context.getCurrencyTypeIds());
        }
        if (null != context.getSupplierIds() && context.getSupplierIds().length > 0) {
            queryWrapper.in("supplier_id", context.getSupplierIds());
        }

        queryWrapper.eq(null != context.getBillDate(), "bill_date", context.getBillDate());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());
        queryWrapper.ge(null != context.getCheckDateStart(), "check_time", context.getCheckDateStart());
        queryWrapper.le(null != context.getCheckDateEnd(), "check_time", context.getCheckDateEnd());

        return queryWrapper;
    }

    /**
     * 处理查询结果的属性
     *
     * @param list
     * @return
     */
    private List<PurchaseBillQueryResult> convertQueryResult(List<PurchaseBill> list) {
        List<PurchaseBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        // 来货超差类型
        Map<Object, String> receiveDifferentTypeMap = getReceiveDifferentTypeList().stream().collect(Collectors.toMap(IdNameDto::getId, v -> v.getName(), (x1, x2) -> x1));
        List<Long> billIdList = list.stream().map(PurchaseBill::getId).distinct().collect(Collectors.toList());
        // 供应商
        Map<Long, IdNameCodeDto> supplierMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Supplier>().in("id", StreamUtil.toSet(list, PurchaseBill::getSupplierId)), Supplier.class, LanguageTableEnum.SUPPLIER);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, PurchaseBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 供货类型
        Map<Object, IdNameDto> provideGoodsTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<ProvideGoodsType>().in("id", StreamUtil.toSet(list, PurchaseBill::getProvideGoodsTypeId)), ProvideGoodsType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, PurchaseBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<PurchaseBillSizeFinal> purchaseBillSizeList = Optional.ofNullable(purchaseBillSizeFinalDao.selectList(new LambdaQueryWrapper<PurchaseBillSizeFinal>().in(PurchaseBillSizeFinal::getBillId, billIdList))).orElse(new ArrayList<>());
        List<PurchaseBillGoodsFinal> purchaseBillGoodsList = Optional.ofNullable(purchaseBillGoodsFinalDao.selectList(new LambdaQueryWrapper<PurchaseBillGoodsFinal>().in(PurchaseBillGoodsFinal::getBillId, billIdList))).orElse(new ArrayList<>());
        // 根据单据分组
        Map<Long, List<PurchaseBillSizeFinal>> billSizeMap = purchaseBillSizeList.stream().collect(Collectors.groupingBy(PurchaseBillSizeFinal::getBillId));
        Map<Long, List<PurchaseBillGoodsFinal>> billGoodsMap = purchaseBillGoodsList.stream().collect(Collectors.groupingBy(PurchaseBillGoodsFinal::getBillId));
        Map<Long, String> goodsMap = new HashMap<>();
        Map<Long, String> colorMap = new HashMap<>();
        Map<Long, String> longMap = new HashMap<>();
        Map<Long, String> sizeMap = new HashMap<>();
        Map<String, String> barcodeMap = new HashMap<>();
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = new HashMap<>();
        if (CollUtil.isNotEmpty(purchaseBillGoodsList)) {
            // 货品
            List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(purchaseBillGoodsList, PurchaseBillGoodsFinal::getGoodsId)));
            goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
            // 颜色
            List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseBillSizeFinal::getColorId)));
            colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
            // 内长
            List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseBillSizeFinal::getLongId)));
            longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
            // 尺码
            List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(purchaseBillSizeList, PurchaseBillSizeFinal::getSizeId)));
            sizeMap = sizeList.stream().collect(Collectors.toMap(SizeDetail::getId, SizeDetail::getName));
            // 条码,默认取第一个
            List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>()
                    .in(CollUtil.isNotEmpty(goodsMap.keySet()), "goods_id", goodsMap.keySet())
                    .in(CollUtil.isNotEmpty(colorMap.keySet()), "color_id", colorMap.keySet())
                    .in(CollUtil.isNotEmpty(longMap.keySet()), "long_id", longMap.keySet())
                    .in(CollUtil.isNotEmpty(sizeMap.keySet()), "size_id", sizeMap.keySet())
                    .orderByDesc("barcode").groupBy("goods_id,color_id,long_id,size_id"));
            barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getSingleCode, Barcode::getBarcode));
            // 货品自定义字段
            goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_BILL_GOODS_FINAL, StreamUtil.toList(purchaseBillGoodsList, PurchaseBillGoodsFinal::getId));
        }
        // 模块自定义字段定义
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(StreamUtil.toList(list, PurchaseBill::getModuleId));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.PURCHASE_BILL, billIdList);
        // 填充
        for (PurchaseBill bill : list) {
            PurchaseBillQueryResult queryResult = new PurchaseBillQueryResult();
            queryResults.add(queryResult);

            queryResult.setModuleId(bill.getModuleId());
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setDeliveryDate(bill.getDeliveryDate());
            queryResult.setProvideGoodsType(OptionalUtil.ofNullable(provideGoodsTypeMap.get(bill.getProvideGoodsTypeId()), IdNameDto::getName));
            queryResult.setSupplierCode(OptionalUtil.ofNullable(supplierMap.get(bill.getSupplierId()), IdNameCodeDto::getCode));
            queryResult.setBusinessType(OptionalUtil.ofNullable(businessTypeMap.get(bill.getBusinessTypeId()), IdNameDto::getName));
            queryResult.setCurrencyType(OptionalUtil.ofNullable(currencyTypeMap.get(bill.getCurrencyTypeId()), IdNameDto::getName));
            queryResult.setStatus(bill.getStatus());
            queryResult.setNotes(bill.getNotes());
            queryResult.setBillDate(bill.getBillDate());
            queryResult.setCheckTime(bill.getCheckTime());
            queryResult.setCreatedTime(bill.getCreatedTime());
            queryResult.setUpdatedTime(bill.getUpdatedTime());
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(bill.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            queryResult.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(bill.getId())));
            // 货品不存在则跳过
            if (CollUtil.isEmpty(purchaseBillGoodsList)) {
                continue;
            }
            // 货品列表
            List<PurchaseBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<PurchaseBillGoodsFinal> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, PurchaseBillGoodsFinal> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(PurchaseBillGoodsFinal::getId, Function.identity()));
            // 尺码明细
            List<PurchaseBillSizeFinal> billSizeList = billSizeMap.get(bill.getId());
            for (PurchaseBillSizeFinal size : billSizeList) {
                PurchaseBillGoodsFinal billGoods = currentGoodsMap.get(size.getBillGoodsId());
                PurchaseBillGoodsDetailData detailData = new PurchaseBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(billGoods.getId())));
                goodsQueryResultList.add(detailData);

                detailData.setReceiveDifferentPercent(billGoods.getReceiveDifferentPercent());
                detailData.setReceiveDifferentType(receiveDifferentTypeMap.get(billGoods.getReceiveDifferentType()));
                detailData.setColumnId(size.getId());
                detailData.setDeliveryDate(billGoods.getDeliveryDate());
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

    /**
     * 返回非空信息
     *
     * @param key
     * @return
     */
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
