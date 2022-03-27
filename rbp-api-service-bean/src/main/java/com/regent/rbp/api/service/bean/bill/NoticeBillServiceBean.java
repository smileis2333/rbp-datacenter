package com.regent.rbp.api.service.bean.bill;

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
import com.regent.rbp.api.core.base.PriceType;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.noticeBill.NoticeBillLogistics;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoodsFinal;
import com.regent.rbp.api.core.salePlan.SalePlanBillSizeFinal;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillGoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillLogisticsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillSizeDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillGoodsFinalDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillSizeFinalDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.notice.NoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.notice.NoticeBillQueryParam;
import com.regent.rbp.api.dto.notice.NoticeBillQueryResult;
import com.regent.rbp.api.dto.notice.NoticeBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.notice.NoticeBillService;
import com.regent.rbp.api.service.notice.context.NoticeBillQueryContext;
import com.regent.rbp.api.service.notice.context.NoticeBillSaveContext;
import com.regent.rbp.common.model.basic.dto.BalanceDetailSampleDto;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockLockDetail;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.common.service.stock.UsableStockDetailService;
import com.regent.rbp.common.service.stock.UsableStockLockDetailService;
import com.regent.rbp.common.utils.StockUtils;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.NumberUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/12/07
 * @description 指令单 实现类
 */
@Service
public class NoticeBillServiceBean extends ServiceImpl<NoticeBillDao, NoticeBill> implements NoticeBillService {

    @Autowired
    private NoticeBillDao noticeBillDao;
    @Autowired
    private NoticeBillSizeDao noticeBillSizeDao;
    @Autowired
    private NoticeBillGoodsDao noticeBillGoodsDao;
    @Autowired
    private NoticeBillLogisticsDao noticeBillLogisticsDao;
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
    private ChannelDao channelDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private SalePlanBillSizeFinalDao salePlanBillSizeFinalDao;
    @Autowired
    private SalePlanBillGoodsFinalDao salePlanBillGoodsFinalDao;
    @Autowired
    private SalePlanBillDao salePlanBillDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private DbService dbService;
    @Autowired
    private UsableStockDetailService usableStockDetailService;
    @Autowired
    private UsableStockLockDetailService usableStockLockDetailService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<NoticeBillQueryResult> query(NoticeBillQueryParam param) {
        NoticeBillQueryContext context = new NoticeBillQueryContext();
        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);
        //查询数据
        PageDataResponse<NoticeBillQueryResult> result = new PageDataResponse<>();
        Page<NoticeBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);
        queryWrapper.orderByDesc("updated_time");
        IPage<NoticeBill> salesPageData = noticeBillDao.selectPage(pageModel, queryWrapper);
        List<NoticeBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

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
    public DataResponse save(NoticeBillSaveParam param) {
        NoticeBillSaveContext context = new NoticeBillSaveContext();
        // 校验并转换
        List<String> messageList = new ArrayList<>();
        this.convertSaveContext(context, param, messageList);
        if (CollUtil.isNotEmpty(messageList)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList.stream().distinct().collect(Collectors.toList()))}));
        }
        NoticeBill bill = context.getBill();
        List<NoticeBillGoods> billGoodsList = context.getBillGoodsList();
        List<NoticeBillSize> billSizeList = context.getBillSizeList();
        List<SalePlanBillSizeFinal> updateSizeFinals = context.getUpdateSalePlanBillSizeFinalList();
        NoticeBillLogistics logistics = context.getLogistics();

        // 获取单号
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setProcessStatus(StatusEnum.NONE.getStatus());
        bill.setFlowStatus(StatusEnum.NONE.getStatus());
        // 新增订单
        noticeBillDao.insert(bill);
        // 核算设置
        this.balanceTempSetting(context);
        // 库存调整
        this.updateStock(context);
        // 单据自定义字段
        baseDbService.saveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.NOTICE_BILL, bill.getId(), bill.getCustomFieldMap());
        // 新增物流信息
        noticeBillLogisticsDao.insert(logistics);
        // 货品自定义字段
        List<Map<String, Object>> customFieldMapList = billGoodsList.stream().map(v -> v.getCustomFieldMap()).filter(f -> null != f).collect(Collectors.toList());
        baseDbService.batchSaveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.NOTICE_BILL_GOODS, customFieldMapList);
        // 批量新增货品明细
        List<NoticeBillGoods> goodsList = new ArrayList<>();
        int i = 0;
        for (NoticeBillGoods goods : billGoodsList) {
            i++;
            goodsList.add(goods);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billGoodsList.size()) {
                noticeBillDao.batchInsertGoodsList(goodsList);
                goodsList.clear();
            }
        }
        // 批量新增尺码明细
        List<NoticeBillSize> sizeList = new ArrayList<>();
        i = 0;
        for (NoticeBillSize size : billSizeList) {
            i++;
            sizeList.add(size);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billSizeList.size()) {
                noticeBillDao.batchInsertSizeList(sizeList);
                sizeList.clear();
            }
        }
        // 更新计划单欠数
        if (CollUtil.isNotEmpty(updateSizeFinals)) {
            String tmpTableName = "rbp_sale_plan_bill_size_temp" + StringUtil.getUUID();
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE `" + tmpTableName + "`");
                sb.append("( `id` BIGINT(20) NOT NULL,");
                sb.append(" `owe_quantity` DECIMAL(14,4),");
                sb.append("  primary key (`id`) ");
                sb.append(" )  default charset=utf8 comment='临时表'");
                // 创建临时表
                baseDbDao.insertSql(sb.toString());
                // 插入临时数据
                noticeBillDao.batchInsertSalePlanBillSizeList(tmpTableName, updateSizeFinals);
                // 批量更新计划单尺码明细
                sb.setLength(0);
                sb.append(" update rbp_sale_plan_bill_size_final a inner join " + tmpTableName + " b on b.id=a.id");
                sb.append(" set a.owe_quantity = b.owe_quantity");
                baseDbDao.updateSql(sb.toString());
            } finally {
                baseDbDao.dropTemplateTable(tmpTableName);
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
    private void convertSaveContext(NoticeBillSaveContext context, NoticeBillSaveParam param, List<String> messageList) {
        /****************   订单主体    ******************/
        NoticeBill bill = new NoticeBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        context.setBill(bill);
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        // 计划单
        Map<String, SalePlanBillGoodsFinal> goodsFinalMap = new HashMap<>();
        if (StringUtil.isNotEmpty(param.getSalePlanNo())) {
            Long salePlanId = baseDbDao.getLongDataBySql(String.format("select id from rbp_sale_plan_bill where status=1 and bill_no = '%s'", param.getSalePlanNo()));
            if (null == salePlanId) {
                messageList.add(getNotExistMessage("salePlanNo"));
            } else {
                bill.setSalePlanId(salePlanId);
                List<SalePlanBillGoodsFinal> finalGoodsList = salePlanBillGoodsFinalDao.selectList(new QueryWrapper<SalePlanBillGoodsFinal>().eq("bill_id", bill.getSalePlanId()));
                List<SalePlanBillSizeFinal> finalSizeList = salePlanBillSizeFinalDao.selectList(new QueryWrapper<SalePlanBillSizeFinal>().eq("bill_id", bill.getSalePlanId()));
                if (CollUtil.isEmpty(finalGoodsList) || CollUtil.isEmpty(finalSizeList)) {
                    messageList.add(getNotExistMessage("salePlanBillGoodsList"));
                } else {
                    finalGoodsList.forEach(item -> item.setFinalSizeList(finalSizeList.stream().filter(f -> f.getBillGoodsId().equals(item.getId())).collect(Collectors.toList())));
                    // 根据货品ID+价格分组
                    goodsFinalMap = finalGoodsList.stream().collect(Collectors.toMap(v -> v.getSameGoodsDiffPriceKey(), Function.identity()));
                }
            }
        }
        // 业务类型
        if (StringUtil.isNotEmpty(param.getBusinessType())) {
            bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        }
        // 价格类型
        if (StringUtil.isNotEmpty(param.getPriceType())) {
            bill.setPriceTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_price_type where name = '%s'", param.getPriceType())));
        }
        // 币种
        if (StringUtil.isNotEmpty(param.getCurrencyType())) {
            bill.setCurrencyTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_currency_type where status = 100 and name = '%s'", param.getCurrencyType())));
        }
        // 发货渠道编码
        if (StringUtil.isNotEmpty(param.getChannelCode())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
        }
        // 收货渠道编码
        if (StringUtil.isNotEmpty(param.getToChannelCode())) {
            bill.setToChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getToChannelCode())));
        }
        // TODO 科目编码
        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            bill.setCustomFieldMap(customFieldMap);
        }
        /****************   物流信息    ******************/
        NoticeBillLogistics logistics = new NoticeBillLogistics();
        context.setLogistics(logistics);
        logistics.preInsert();
        logistics.setBillId(bill.getId());
        logistics.setCity(param.getCity());
        logistics.setCounty(param.getCounty());
        logistics.setNation(param.getNation());
        logistics.setProvince(param.getProvince());
        logistics.setAddress(param.getAddress());
        logistics.setContactsPerson(param.getContactsPerson());
        logistics.setLogisticsBillCode(param.getLogisticsBillCode());
        logistics.setMobile(param.getMobile());
        logistics.setPostCode(param.getPostCode());
        logistics.setNotes(param.getLogisticsNotes());
        // 物流公司
        if (StringUtil.isNotEmpty(param.getLogisticsCompanyCode())) {
            logistics.setLogisticsCompanyId(baseDbDao.getLongDataBySql(String.format("select id from rbp_logistics_company where status = 100 and code = '%s'", param.getLogisticsCompanyCode())));
        }
        // 订单主体校验
        if (null == bill.getBusinessTypeId()) {
            messageList.add(getNotNullMessage("businessTypeId"));
        }
        // 判断手工单号是否重复
        if (messageList.size() == 0) {
            Integer count = noticeBillDao.selectCount(new QueryWrapper<NoticeBill>().eq("manual_id", bill.getManualId()));
            if (null != count && count > 0) {
                messageList.add(getMessageByParams("dataExist", new String[]{LanguageUtil.getMessage("manualId")}));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        /****************   货品明细    ******************/
        List<NoticeBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        List<NoticeBillSize> billSizeList = new ArrayList<>();
        context.setBillSizeList(billSizeList);
        List<SalePlanBillSizeFinal> updateSizeFinalList = new ArrayList<>();
        context.setUpdateSalePlanBillSizeFinalList(updateSizeFinalList);
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
            Integer type = goodsList.stream().filter(e -> e.getCode().equals(param.getGoodsDetailData().get(0).getGoodsCode())).findFirst().get().getType();
            List<Color> colorList = type == 2 ? Collections.emptyList() : colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getColorCode())));
            colorMap = colorList.stream().collect(Collectors.toMap(Color::getCode, Color::getId, (x1, x2) -> x1));
            List<LongInfo> longList = type == 2 ? Collections.emptyList() : longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getLongName())));
            longMap = longList.stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId, (x1, x2) -> x1));
            List<SizeDetail> sizeClassList = type == 2 ? Collections.emptyList() : baseDbDao.getSizeNameList(StreamUtil.toList(goodsList, Goods::getId), StreamUtil.toList(param.getGoodsDetailData(), v -> v.getSize()));
            sizeMap = type == 2 ? Collections.emptyMap() : sizeClassList.stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));
        }
        // 尺码明细
        List<NoticeBillSize> sizeList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (NoticeBillGoodsDetailData item : param.getGoodsDetailData()) {
            atomicInteger.getAndIncrement();
            NoticeBillSize size = new NoticeBillSize();
            sizeList.add(size);

            size.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            size.setBillId(bill.getId());
            size.setQuantity(item.getQuantity());
            size.setOweQuantity(item.getQuantity());
            Barcode barcode = barcodeMap.get(item.getBarcode());
            if (null != barcode) {
                size.setGoodsId(barcode.getGoodsId());
                size.setLongId(barcode.getLongId());
                size.setColorId(barcode.getColorId());
                size.setSizeId(barcode.getSizeId());
            } else {
                size.setGoodsId(goodsMap.get(item.getGoodsCode()));
                size.setLongId(longMap.getOrDefault(item.getLongName(), 1200000000000003L));
                size.setColorId(colorMap.getOrDefault(item.getColorCode(), 1200000000000002L));
                size.setSizeId(sizeMap.getOrDefault(size.getGoodsId() + StrUtil.UNDERLINE + item.getSize(), 1200000000000005L));
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
        for (NoticeBillGoodsDetailData item : param.getGoodsDetailData()) {
            // TODO 结算价不存在,则通过分销价格获取
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId())) {

            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "balancePrice"));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // 尺码根据行ID分组
        Map<Long, NoticeBillSize> noticeBillSizeMap = sizeList.stream().collect(Collectors.toMap(v -> v.getId(), Function.identity()));

        // 存在计划单，根据货品+结算价匹配,获取计划单货品ID和欠数，不存在或超出欠数报错，更新计划单欠数
        Map<String, SalePlanBillGoodsFinal> finalGoodsFinalMap = goodsFinalMap;
        atomicInteger.set(0);
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
            atomicInteger.getAndIncrement();
            NoticeBillGoodsDetailData detailData = sizes.get(0);
            SalePlanBillGoodsFinal goodsFinal = finalGoodsFinalMap.get(key);
            if (null != bill.getSalePlanId() && null == goodsFinal) {
                messageList.add(getMessageByParams("notReferencedBillGoods", new Object[]{sizes.get(0).getGoodsCode()}));
            } else {
                NoticeBillGoods billGoods = NoticeBillGoods.build();
                Map<String, SalePlanBillSizeFinal> sizeFinalMap = new HashMap<>();
                // 引用销售计划
                if (null != bill.getSalePlanId()) {
                    billGoods.setSalePlanGoodsId(goodsFinal.getId());
                    sizeFinalMap = goodsFinal.getFinalSizeList().stream().collect(Collectors.toMap(SalePlanBillSizeFinal::getSingleCode, Function.identity()));
                }
                billGoodsList.add(billGoods);

                billGoods.setBillId(bill.getId());
                billGoods.setGoodsId(detailData.getGoodsId());
                billGoods.setTagPrice(detailData.getTagPrice());
                billGoods.setBalancePrice(detailData.getBalancePrice());
                billGoods.setDiscount(detailData.getDiscount());
                billGoods.setCurrencyPrice(detailData.getCurrencyPrice());
                billGoods.setExchangeRate(detailData.getExchangeRate());
                billGoods.setQuantity(sizes.stream().map(v -> Optional.ofNullable(v.getQuantity()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add));
                billGoods.setRemark(detailData.getRemark());
                // 自定义字段
                if (CollUtil.isNotEmpty(detailData.getGoodsCustomizeData())) {
                    Map<String, Object> customFieldMap = new HashMap<>();
                    customFieldMap.put("id", billGoods.getId());
                    detailData.getGoodsCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
                    billGoods.setCustomFieldMap(customFieldMap);
                }
                // 尺码明细
                for (NoticeBillGoodsDetailData item : sizes) {
                    NoticeBillSize billSize = noticeBillSizeMap.get(item.getColumnId());
                    SalePlanBillSizeFinal sizeFinal = sizeFinalMap.get(billSize.getSingleCode());
                    // 引用销售计划
                    if (null != bill.getSalePlanId()) {
                        if (null == sizeFinal) {
                            messageList.add(getMessageByParams("notReferencedBillSize", new Object[]{item.getGoodsCode() + StrUtil.COLON + item.getSize()}));
                            continue;
                        }
                        // 校验计划单欠数
                        BigDecimal oweQuantity = sizeFinal.getOweQuantity().add(billSize.getQuantity().negate());
                        if (oweQuantity.compareTo(BigDecimal.ZERO) < 0) {
                            messageList.add(getMessageByParams("billGoodsGtOweQuantity", new Object[]{item.getGoodsCode() + StrUtil.COLON + item.getSize()}));
                            continue;
                        }
                        sizeFinal.setOweQuantity(oweQuantity);
                        updateSizeFinalList.add(sizeFinal);
                    }
                    billSizeList.add(billSize);
                    billSize.setBillGoodsId(billGoods.getId());
                }
            }
        });

    }

    /**
     * 将查询参数转换成 查询的上下文
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(NoticeBillQueryParam param, NoticeBillQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, NoticeBillQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, NoticeBillQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setModuleId(param.getModuleId());
        context.setManualId(param.getManualId());
        context.setBillNo(param.getBillNo());
        context.setNotes(param.getNotes());
        context.setStatus(param.getStatus());
        context.setFields(param.getFields());
        // 计划单
        if (StringUtil.isNotEmpty(param.getSalePlanNo())) {
            context.setSalePlanId(baseDbDao.getLongDataBySql(String.format("select id from rbp_sale_plan_bill where bill_no = '%s'", param.getSalePlanNo())));
        }
        // 收货渠道
        if (param.getChannelCode() != null && param.getChannelCode().length > 0) {
            List<Channel> idList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getChannelCode()));
            if (CollUtil.isNotEmpty(idList)) {
                context.setChannelIds(idList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
        // 发货渠道
        if (param.getToChannelCode() != null && param.getToChannelCode().length > 0) {
            List<Channel> idList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getToChannelCode()));
            if (CollUtil.isNotEmpty(idList)) {
                context.setToChannelIds(idList.stream().mapToLong(map -> map.getId()).toArray());
            }
        }
        // 业务类型
        if (param.getBusinessType() != null && param.getBusinessType().length > 0) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_business_type where status=100 %s", AppendSqlUtil.getInStrSql("name", Arrays.asList(param.getBusinessType()))));
            if (CollUtil.isNotEmpty(idList)) {
                context.setBusinessTypeIds(idList.stream().mapToLong(Long::longValue).toArray());
            }
        }
        // 价格类型
        if (param.getPriceType() != null && param.getPriceType().length > 0) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_price_type where 1=1 %s", AppendSqlUtil.getInStrSql("name", Arrays.asList(param.getPriceType()))));
            if (CollUtil.isNotEmpty(idList)) {
                context.setPriceTypeIds(idList.stream().mapToLong(Long::longValue).toArray());
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
     * 整理查询条件构造器
     *
     * @param context
     * @return
     */
    private QueryWrapper processQueryWrapper(NoticeBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<NoticeBill>();

        queryWrapper.eq(StringUtil.isNotEmpty(context.getModuleId()), "module_id", context.getModuleId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getManualId()), "manual_id", context.getManualId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getBillNo()), "bill_no", context.getBillNo());
        queryWrapper.eq(null != context.getSalePlanId(), "sale_plan_id", context.getSalePlanId());
        queryWrapper.in(null != context.getStatus() && context.getStatus().length > 0, "status", context.getStatus());
        if (null != context.getBusinessTypeIds() && context.getBusinessTypeIds().length > 0) {
            queryWrapper.in("business_type_id", context.getBusinessTypeIds());
        }
        if (null != context.getPriceTypeIds() && context.getPriceTypeIds().length > 0) {
            queryWrapper.in("price_type_id", context.getPriceTypeIds());
        }
        if (null != context.getCurrencyTypeIds() && context.getCurrencyTypeIds().length > 0) {
            queryWrapper.in("currency_type_id", context.getCurrencyTypeIds());
        }
        if (null != context.getChannelIds() && context.getChannelIds().length > 0) {
            queryWrapper.in("channel_id", context.getChannelIds());
        }
        if (null != context.getToChannelIds() && context.getToChannelIds().length > 0) {
            queryWrapper.in("to_channel_id", context.getToChannelIds());
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
    private List<NoticeBillQueryResult> convertQueryResult(List<NoticeBill> list) {
        List<NoticeBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(NoticeBill::getId).distinct().collect(Collectors.toList());
        // 发货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, NoticeBill::getChannelId);
        channelIds.addAll(StreamUtil.toSet(list, NoticeBill::getToChannelId));
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, NoticeBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 价格类型
        Map<Object, IdNameDto> priceTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<PriceType>().in("id", StreamUtil.toSet(list, NoticeBill::getPriceTypeId)), PriceType.class, LanguageTableEnum.PRICETYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, NoticeBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<NoticeBillSize> noticeBillSizeList = noticeBillSizeDao.selectList(new LambdaQueryWrapper<NoticeBillSize>().in(NoticeBillSize::getBillId, billIdList));
        List<NoticeBillGoods> noticeBillGoodsList = noticeBillGoodsDao.selectList(new LambdaQueryWrapper<NoticeBillGoods>().in(NoticeBillGoods::getBillId, billIdList));
        // 根据单据分组
        Map<Long, List<NoticeBillSize>> billSizeMap = noticeBillSizeList.stream().collect(Collectors.groupingBy(NoticeBillSize::getBillId));
        Map<Long, List<NoticeBillGoods>> billGoodsMap = noticeBillGoodsList.stream().collect(Collectors.groupingBy(NoticeBillGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(noticeBillGoodsList, NoticeBillGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(noticeBillSizeList, NoticeBillSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(noticeBillSizeList, NoticeBillSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(noticeBillSizeList, NoticeBillSize::getSizeId)));
        Map<Long, String> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeDetail::getId, SizeDetail::getName));
        // 条码,默认取第一个
        List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>()
                .in(CollUtil.isNotEmpty(goodsMap.keySet()), "goods_id", goodsMap.keySet())
                .in(CollUtil.isNotEmpty(colorMap.keySet()), "color_id", colorMap.keySet())
                .in(CollUtil.isNotEmpty(longMap.keySet()), "long_id", longMap.keySet())
                .in(CollUtil.isNotEmpty(sizeMap.keySet()), "size_id", sizeMap.keySet())
                .orderByDesc("barcode").groupBy("goods_id,color_id,long_id,size_id"));
        Map<String, String> barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getSingleCode, Barcode::getBarcode));
        // 计划单
        Map<Long, String> salePlanNoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(StreamUtil.toSet(list, NoticeBill::getSalePlanId))) {
            List<SalePlanBill> salePlanBillList = salePlanBillDao.selectList(new LambdaQueryWrapper<SalePlanBill>().in(SalePlanBill::getId, StreamUtil.toSet(list, NoticeBill::getSalePlanId)));
            salePlanNoMap = salePlanBillList.stream().collect(Collectors.toMap(SalePlanBill::getId, SalePlanBill::getBillNo));
        }
        // 物流公司
        Map<Long, NoticeBillLogistics> logisticsMap = new HashMap<>();
        Map<Long, String> logisticsCompanyMap = new HashMap<>();
        List<NoticeBillLogistics> logisticsList = noticeBillLogisticsDao.selectBatchIds(billIdList);
        if (CollUtil.isNotEmpty(logisticsList)) {
            logisticsMap = logisticsList.stream().collect(Collectors.toMap(NoticeBillLogistics::getBillId, Function.identity()));
            List<Object> ids = logisticsList.stream().map(NoticeBillLogistics::getLogisticsCompanyId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(ids)) {
                List<Map<Object, Object>> mapList = baseDbDao.getListMap(String.format("select id,code from rbp_logistics_company where status=100 and id in %s", AppendSqlUtil.getInSql(ids)));
                mapList.forEach(item -> logisticsCompanyMap.put((Long) item.get("id"), (String) item.get("code")));
            }
        }
        // 模块自定义字段定义
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(StreamUtil.toList(list, NoticeBill::getModuleId));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.NOTICE_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.NOTICE_BILL_GOODS, StreamUtil.toList(noticeBillGoodsList, NoticeBillGoods::getId));
        // 填充
        for (NoticeBill bill : list) {
            NoticeBillQueryResult queryResult = new NoticeBillQueryResult();
            queryResults.add(queryResult);

            queryResult.setModuleId(bill.getModuleId());
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getChannelId()), IdNameCodeDto::getCode));
            queryResult.setToChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getToChannelId()), IdNameCodeDto::getCode));
            queryResult.setPriceType(OptionalUtil.ofNullable(priceTypeMap.get(bill.getPriceTypeId()), IdNameDto::getName));
            queryResult.setBusinessType(OptionalUtil.ofNullable(businessTypeMap.get(bill.getBusinessTypeId()), IdNameDto::getName));
            queryResult.setCurrencyType(OptionalUtil.ofNullable(currencyTypeMap.get(bill.getCurrencyTypeId()), IdNameDto::getName));
            queryResult.setSalePlanNo(salePlanNoMap.get(bill.getSalePlanId()));
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
            // 物流信息
            NoticeBillLogistics logistics = logisticsMap.get(bill.getId());
            if (null != logistics) {
                queryResult.setLogisticsCompanyCode(logisticsCompanyMap.get(logistics.getLogisticsCompanyId()));
                queryResult.setLogisticsBillCode(logistics.getLogisticsBillCode());
                queryResult.setNation(logistics.getNation());
                queryResult.setProvince(logistics.getProvince());
                queryResult.setCity(logistics.getCity());
                queryResult.setCounty(logistics.getCounty());
                queryResult.setAddress(logistics.getAddress());
                queryResult.setContactsPerson(logistics.getContactsPerson());
                queryResult.setMobile(logistics.getMobile());
                queryResult.setPostCode(logistics.getPostCode());
                queryResult.setLogisticsNotes(logistics.getNotes());
            }
            // 货品列表
            List<NoticeBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<NoticeBillGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, NoticeBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(NoticeBillGoods::getId, Function.identity()));
            // 尺码明细
            List<NoticeBillSize> billSizeList = billSizeMap.get(bill.getId());
            for (NoticeBillSize size : billSizeList) {
                NoticeBillGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                NoticeBillGoodsDetailData detailData = new NoticeBillGoodsDetailData();
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

    /**
     * 核算设置
     * 指令单不需要结存，只存临时核算明细
     *
     * @param context
     */
    private void balanceTempSetting(NoticeBillSaveContext context) {
        // 单据主体
        BalanceDetailSampleDto sampleDto = new BalanceDetailSampleDto();
        NoticeBill bill = context.getBill();
        sampleDto.setModuleId(bill.getModuleId());
        sampleDto.setBillId(bill.getId());
        sampleDto.setManualId(bill.getManualId());
        sampleDto.setBillNo(bill.getBillNo());
        sampleDto.setBillDate(bill.getBillDate());
        sampleDto.setBusinessTypeId(bill.getBusinessTypeId());
        sampleDto.setChannelId(bill.getChannelId());
        sampleDto.setToChannelId(bill.getToChannelId());
        sampleDto.setPriceTypeId(bill.getPriceTypeId());
        sampleDto.setNotes(bill.getNotes());
        // 货品明细
        List<NoticeBillGoods> billGoodsList = context.getBillGoodsList();
        List<BalanceDetailSampleDto> goodsList = new ArrayList<>();
        for (NoticeBillGoods goods : billGoodsList) {
            BalanceDetailSampleDto dto = new BalanceDetailSampleDto();
            goodsList.add(dto);
            dto.setBillGoodsId(goods.getId());
            dto.setGoodsId(goods.getGoodsId());
            dto.setQuantity(goods.getQuantity());
            dto.setTagPrice(goods.getTagPrice());
            dto.setTagAmount(NumberUtil.mul(goods.getQuantity(), goods.getTagPrice()));
            dto.setPrice(goods.getBalancePrice());
            dto.setAmount(NumberUtil.mul(goods.getQuantity(), goods.getBalancePrice()));
            dto.setDiscount(goods.getDiscount());
        }
        // 创建临时核算
        systemCommonService.insertGenerateReceivableBalanceTempDetail(sampleDto, goodsList, false);
    }

    /**
     * 库存调整
     *
     * @param context
     */
    private void updateStock(NoticeBillSaveContext context) {
        NoticeBill bill = context.getBill();
        List<NoticeBillSize> billSizeList = context.getBillSizeList();
        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(bill.getChannelId());

        //按单据明细增加可用库存占用数
        List<UsableStockLockDetail> usableStockLockDetails = this.getUsableStockLockDetails(bill, billSizeList, true);
        if (usableStockLockDetails.size() > 0) {
            usableStockLockDetailService.insertUsableStockLockDetailList(usableStockLockDetails);
        }

        //按单据明细减少可用库存
        Set<UsableStockDetail> usableStockDetails = this.getUsableStockDetails(bill.getChannelId(), billSizeList);
        usableStockDetailService.decreaseUsableStockDetail(usableStockDetails, isMustPositive);
    }

    /**
     * 获取需要调整的可用库存占用明细
     *
     * @param noticeBill
     * @param noticeBillSizeList
     * @param increaseFlag
     * @return
     */
    private List<UsableStockLockDetail> getUsableStockLockDetails(NoticeBill noticeBill, List<NoticeBillSize> noticeBillSizeList, boolean increaseFlag) {
        List<UsableStockLockDetail> usableStockLockDetails = new ArrayList<>(noticeBillSizeList.size());
        for (NoticeBillSize billSize : noticeBillSizeList) {
            UsableStockLockDetail usableStockLockDetail = new UsableStockLockDetail();
            BeanUtils.copyProperties(billSize, usableStockLockDetail);
            usableStockLockDetail.setChannelId(noticeBill.getChannelId());
            //来源单据是发货单
            usableStockLockDetail.setSourceModuleId(noticeBill.getModuleId());
            usableStockLockDetail.setSourceBillId(noticeBill.getId());
            usableStockLockDetail.setSourceBillNo(noticeBill.getBillNo());

            usableStockLockDetail.setModuleId(noticeBill.getModuleId());
            usableStockLockDetail.setBillId(noticeBill.getId());
            usableStockLockDetail.setBillNo(noticeBill.getBillNo());

            if (increaseFlag) {
                //增加占用数(正数，不引用单据的情况下)
                usableStockLockDetail.setQuantity(billSize.getQuantity());
            } else {
                //减少占用数（负数对冲 指令单的占用数）
                usableStockLockDetail.setQuantity(billSize.getQuantity().negate());
            }
            usableStockLockDetail.setHashCode(StockUtils.calculateHashCode(usableStockLockDetail));
            usableStockLockDetail.setSkuHashCode(StockUtils.calculateSkuHashCode(usableStockLockDetail));

            usableStockLockDetails.add(usableStockLockDetail);
        }
        return usableStockLockDetails;
    }

    /**
     * 获取需要调整的可用库存明细
     *
     * @param channelId
     * @param noticeBillSizeList
     * @return
     */
    private Set<UsableStockDetail> getUsableStockDetails(Long channelId, List<NoticeBillSize> noticeBillSizeList) {
        Hashtable<String, UsableStockDetail> hashUsableStockDetails = new Hashtable<>();
        for (NoticeBillSize billSize : noticeBillSizeList) {
            String hashCode = StockUtils.calculateHashCode(channelId, billSize.getGoodsId(), billSize.getColorId(), billSize.getLongId(), billSize.getSizeId());
            String skuHashCode = StockUtils.calculateSkuHashCode(billSize.getGoodsId(), billSize.getColorId(), billSize.getLongId(), billSize.getSizeId());

            UsableStockDetail usableStockDetail = null;
            BigDecimal quantity = billSize.getQuantity();
            if (hashUsableStockDetails.containsKey(hashCode)) {
                usableStockDetail = hashUsableStockDetails.get(hashCode);
            } else {
                usableStockDetail = new UsableStockDetail();
                BeanUtils.copyProperties(billSize, usableStockDetail);
                usableStockDetail.setChannelId(channelId);
                usableStockDetail.setQuantity(BigDecimal.ZERO);
                usableStockDetail.setReduceQuantity(BigDecimal.ZERO);
                usableStockDetail.setHashCode(hashCode);
                usableStockDetail.setSkuHashCode(skuHashCode);
                hashUsableStockDetails.put(hashCode, usableStockDetail);
            }
            usableStockDetail.setQuantity(usableStockDetail.getQuantity().add(quantity));
        }
        Set<UsableStockDetail> usableStockDetails = hashUsableStockDetails.values().stream().collect(Collectors.toSet());
        hashUsableStockDetails.clear();
        return usableStockDetails;
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
