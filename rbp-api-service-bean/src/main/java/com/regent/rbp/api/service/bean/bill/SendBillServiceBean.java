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
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.core.sendBill.SendBillLogistics;
import com.regent.rbp.api.core.sendBill.SendBillSize;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillGoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillSizeDao;
import com.regent.rbp.api.dao.sendBill.SendBillDao;
import com.regent.rbp.api.dao.sendBill.SendBillGoodsDao;
import com.regent.rbp.api.dao.sendBill.SendBillLogisticsDao;
import com.regent.rbp.api.dao.sendBill.SendBillSizeDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.send.SendBillGoodsDetailData;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.send.SendBillService;
import com.regent.rbp.api.service.send.context.SendBillQueryContext;
import com.regent.rbp.api.service.send.context.SendBillSaveContext;
import com.regent.rbp.common.eum.basic.SendBillReferenceType;
import com.regent.rbp.common.model.basic.dto.BalanceDetailSampleDto;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.model.stock.entity.ForwayStockDetail;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockLockDetail;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.common.service.stock.ForwayStockDetailService;
import com.regent.rbp.common.service.stock.StockDetailService;
import com.regent.rbp.common.service.stock.UsableStockDetailService;
import com.regent.rbp.common.service.stock.UsableStockLockDetailService;
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
 * @date 2021/12/16
 * @description 发货单 实现类
 */
@Service
public class SendBillServiceBean extends ServiceImpl<SendBillDao, SendBill> implements SendBillService {

    @Autowired
    private SendBillDao sendBillDao;
    @Autowired
    private SendBillSizeDao sendBillSizeDao;
    @Autowired
    private SendBillGoodsDao sendBillGoodsDao;
    @Autowired
    private SendBillLogisticsDao sendBillLogisticsDao;
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
    private NoticeBillSizeDao noticeBillSizeDao;
    @Autowired
    private NoticeBillGoodsDao noticeBillGoodsDao;
    @Autowired
    private NoticeBillDao noticeBillDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private DbService dbService;
    @Autowired
    private UsableStockDetailService usableStockDetailService;
    @Autowired
    private UsableStockLockDetailService usableStockLockDetailService;
    @Autowired
    private ForwayStockDetailService forwayStockDetailService;
    @Autowired
    private StockDetailService stockDetailService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<SendBillQueryResult> query(SendBillQueryParam param) {
        SendBillQueryContext context = new SendBillQueryContext();
        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);
        //查询数据
        PageDataResponse<SendBillQueryResult> result = new PageDataResponse<>();
        Page<SendBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);
        queryWrapper.orderByDesc("updated_time");
        IPage<SendBill> salesPageData = sendBillDao.selectPage(pageModel, queryWrapper);
        List<SendBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

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
    public DataResponse save(SendBillSaveParam param) {
        SendBillSaveContext context = new SendBillSaveContext();
        // 校验并转换
        List<String> messageList = new ArrayList<>();
        this.convertSaveContext(context, param, messageList);
        if (CollUtil.isNotEmpty(messageList)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList.stream().distinct().collect(Collectors.toList()))}));
        }
        SendBill bill = context.getBill();
        List<SendBillGoods> billGoodsList = context.getBillGoodsList();
        List<SendBillSize> billSizeList = context.getBillSizeList();
        List<NoticeBillSize> updateSizes = context.getUpdateNoticeBillSizeList();
        SendBillLogistics logistics = context.getLogistics();

        // 获取单号
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setProcessStatus(StatusEnum.NONE.getStatus());
        bill.setFlowStatus(StatusEnum.NONE.getStatus());
        // 新增订单
        sendBillDao.insert(bill);
        // 核算设置
        this.balanceSetting(context);
        // 库存调整
        if (StatusEnum.CHECK.getStatus().equals(bill.getStatus())) {
            this.checkModifyStock(context);
        } else {
            this.updateStock(context);
        }
        // 单据自定义字段
        baseDbService.saveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.SEND_BILL, bill.getId(), bill.getCustomFieldMap());
        // 新增物流信息
        sendBillLogisticsDao.insert(logistics);
        // 货品自定义字段
        List<Map<String, Object>> customFieldMapList = billGoodsList.stream().map(v -> v.getCustomFieldMap()).filter(f -> null != f).collect(Collectors.toList());
        baseDbService.batchSaveOrUpdateCustomFieldData(bill.getModuleId(), TableConstants.SEND_BILL_GOODS, customFieldMapList);
        // 批量新增货品明细
        List<SendBillGoods> goodsList = new ArrayList<>();
        int i = 0;
        for (SendBillGoods goods : billGoodsList) {
            i++;
            goodsList.add(goods);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billGoodsList.size()) {
                sendBillDao.batchInsertGoodsList(goodsList);
                goodsList.clear();
            }
        }
        // 批量新增尺码明细
        List<SendBillSize> sizeList = new ArrayList<>();
        i = 0;
        for (SendBillSize size : billSizeList) {
            i++;
            sizeList.add(size);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billSizeList.size()) {
                sendBillDao.batchInsertSizeList(sizeList);
                sizeList.clear();
            }
        }
        // 更新计划单欠数
        if (CollUtil.isNotEmpty(updateSizes)) {
            String tmpTableName = "rbp_notice_bill_size_temp" + StringUtil.getUUID();
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
                sendBillDao.batchInsertNoticeBillSizeList(tmpTableName, updateSizes);
                // 批量更新计划单尺码明细
                sb.setLength(0);
                sb.append(" update rbp_notice_bill_size a inner join " + tmpTableName + " b on b.id=a.id");
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
    private void convertSaveContext(SendBillSaveContext context, SendBillSaveParam param, List<String> messageList) {
        /****************   订单主体    ******************/
        SendBill bill = new SendBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        context.setBill(bill);
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        // 业务类型
        if (StringUtil.isNotEmpty(param.getBusinessType())) {
            bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
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
        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            bill.setCustomFieldMap(customFieldMap);
        }
        /****************   物流信息    ******************/
        SendBillLogistics logistics = new SendBillLogistics();
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

        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        /****************   货品明细    ******************/
        List<SendBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        List<SendBillSize> billSizeList = new ArrayList<>();
        context.setBillSizeList(billSizeList);
        List<NoticeBillSize> updateSizeList = new ArrayList<>();
        context.setUpdateNoticeBillSizeList(updateSizeList);
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
        // 价格类型
        Map<String, Long> priceTypeMap = new HashMap<>();
        List<IdNameDto> priceTypeList = dbService.selectIdNameList(new QueryWrapper<PriceType>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getPriceType())), PriceType.class);
        if (CollUtil.isNotEmpty(priceTypeList)) {
            priceTypeMap = priceTypeList.stream().collect(Collectors.toMap(IdNameDto::getName, v -> (Long) v.getId(), (x1, x2) -> x1));
        }
        if (StringUtil.isNotEmpty(param.getGoodsDetailData().get(0).getBarcode())) {
            List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getBarcode())));
            barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity(), (x1, x2) -> x1));
        } else {
            List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getGoodsCode())));
            Integer type = goodsList.stream().filter(e -> e.getCode().equals(param.getGoodsDetailData().get(0).getGoodsCode())).findFirst().get().getType();
            goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId, (x1, x2) -> x1));
            List<Color> colorList = type == 2 ? Collections.emptyList() : colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getColorCode())));
            colorMap = colorList.stream().collect(Collectors.toMap(Color::getCode, Color::getId, (x1, x2) -> x1));
            List<LongInfo> longList = type == 2 ? Collections.emptyList() : longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), v -> v.getLongName())));
            longMap = longList.stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId, (x1, x2) -> x1));
            List<SizeDetail> sizeClassList = type == 2 ? Collections.emptyList() : baseDbDao.getSizeNameList(StreamUtil.toList(goodsList, Goods::getId), StreamUtil.toList(param.getGoodsDetailData(), v -> v.getSize()));
            sizeMap = type == 2 ? Collections.emptyMap() : sizeClassList.stream().collect(Collectors.toMap(v -> v.getGoodsId() + StrUtil.UNDERLINE + v.getName(), SizeDetail::getId, (x1, x2) -> x1));
        }
        // 查询指令单货品、尺码数
        Map<String, NoticeBillGoods> noticeGoodsMap = new HashMap<>();
        Set<String> noticeNoSet = param.getGoodsDetailData().stream().filter(f -> StringUtil.isNotEmpty(f.getNoticeNo())).map(v -> v.getNoticeNo()).collect(Collectors.toSet());
        Map<String, NoticeBill> noticeNoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(noticeNoSet)) {
            List<NoticeBill> noticeBills = noticeBillDao.selectList(new QueryWrapper<NoticeBill>().select("id,bill_no,sale_plan_id").in("bill_no", noticeNoSet));
            if (CollUtil.isEmpty(noticeBills) || noticeNoSet.size() > noticeBills.size()) {
                messageList.add(getNotExistMessage("noticeNo"));
            } else {
                noticeNoMap = noticeBills.stream().collect(Collectors.toMap(NoticeBill::getBillNo, Function.identity(), (x1, x2) -> x1));
                List<NoticeBillGoods> noticeBillGoodsList = noticeBillGoodsDao.selectList(new QueryWrapper<NoticeBillGoods>().in("bill_id", StreamUtil.toSet(noticeBills, NoticeBill::getId)));
                List<NoticeBillSize> noticeBillSizeList = noticeBillSizeDao.selectList(new QueryWrapper<NoticeBillSize>().in("bill_id", StreamUtil.toSet(noticeBills, NoticeBill::getId)));
                if (CollUtil.isEmpty(noticeBillGoodsList) || CollUtil.isEmpty(noticeBillSizeList)) {
                    messageList.add(getNotExistMessage("salePlanBillGoodsList"));
                } else {
                    noticeBillGoodsList.forEach(item -> item.setSizeList(noticeBillSizeList.stream().filter(f -> f.getBillGoodsId().equals(item.getId())).collect(Collectors.toList())));
                    // 根据单据ID+货品ID+价格分组
                    noticeGoodsMap = noticeBillGoodsList.stream().collect(Collectors.toMap(v -> v.getSameGoodsDiffPriceKey(), Function.identity()));
                }
            }
        }
        // 尺码明细
        List<SendBillSize> sizeList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (SendBillGoodsDetailData item : param.getGoodsDetailData()) {
            atomicInteger.getAndIncrement();
            SendBillSize size = new SendBillSize();
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
                size.setLongId(longMap.getOrDefault(item.getLongName(), 1200000000000003L));
                size.setColorId(colorMap.getOrDefault(item.getColorCode(), 1200000000000002L));
                size.setSizeId(sizeMap.getOrDefault(size.getGoodsId() + StrUtil.UNDERLINE + item.getSize(), 1200000000000005L));
            }
            if (StrUtil.isNotBlank(item.getPriceType()) && null == priceTypeMap.get(item.getPriceType())) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "priceType"));
            }
            if (CollUtil.isEmpty(messageList)) {
                // 设置货品ID
                item.setGoodsId(size.getGoodsId());
                item.setColumnId(size.getId());
                // 设置指令单ID
                item.setNoticeId(OptionalUtil.ofNullable(noticeNoMap.get(item.getNoticeNo()), NoticeBill::getId));
                item.setSalePlanId(OptionalUtil.ofNullable(noticeNoMap.get(item.getNoticeNo()), NoticeBill::getSalePlanId));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // TODO 不存在结算价的货品
        List<Long> goodsIdList = param.getGoodsDetailData().stream().filter(f -> null == f.getBalancePrice()).map(v -> v.getGoodsId()).distinct().collect(Collectors.toList());
        // TODO 获取分销价格
        atomicInteger.set(0);
        for (SendBillGoodsDetailData item : param.getGoodsDetailData()) {
            // TODO 结算价不存在,则通过分销价格获取
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId())) {

            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "balancePrice"));
            }
        }
        // 尺码根据行ID分组
        Map<Long, SendBillSize> sendBillSizeMap = sizeList.stream().collect(Collectors.toMap(v -> v.getId(), Function.identity()));

        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // 存在指令单，根据单据ID+货品+结算价匹配,获取指令单货品ID和欠数，不存在或超出欠数报错，更新指令单欠数
        Map<String, NoticeBillGoods> finalNoticeGoodsMap = noticeGoodsMap;
        atomicInteger.set(0);
        // 根据货品+价格分组，支持同款多价
        Map<String, Long> finalPriceTypeMap = priceTypeMap;
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
            atomicInteger.getAndIncrement();
            SendBillGoods billGoods = SendBillGoods.build();
            billGoodsList.add(billGoods);

            billGoods.setBillId(bill.getId());
            SendBillGoodsDetailData detailData = sizes.get(0);
            billGoods.setGoodsId(detailData.getGoodsId());
            billGoods.setTagPrice(detailData.getTagPrice());
            billGoods.setPriceTypeId(finalPriceTypeMap.get(detailData.getPriceType()));
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
            // 指令单货品
            NoticeBillGoods noticeBillGoods = finalNoticeGoodsMap.get(key);
            Map<String, NoticeBillSize> sizeFinalMap = new HashMap<>();
            // 引用指令单
            if (null != detailData.getNoticeId() && null == noticeBillGoods) {
                messageList.add(getMessageByParams("notReferencedBillGoods", new Object[]{sizes.get(0).getGoodsCode()}));
            } else {
                if (null != detailData.getNoticeId()) {
                    billGoods.setNoticeId(noticeBillGoods.getBillId());
                    billGoods.setNoticeGoodsId(noticeBillGoods.getId());
                    billGoods.setSalePlanGoodsId(noticeBillGoods.getSalePlanGoodsId());
                    billGoods.setSalePlanId(detailData.getSalePlanId());

                    sizeFinalMap = noticeBillGoods.getSizeList().stream().collect(Collectors.toMap(NoticeBillSize::getSingleCode, Function.identity()));
                }
                // 尺码明细
                for (SendBillGoodsDetailData item : sizes) {
                    SendBillSize billSize = sendBillSizeMap.get(item.getColumnId());
                    // 引用指令单
                    if (StringUtil.isNotEmpty(item.getNoticeNo())) {
                        NoticeBillSize sizeFinal = sizeFinalMap.get(billSize.getSingleCode());
                        if (null == sizeFinal) {
                            messageList.add(getMessageByParams("notReferencedBillSize", new Object[]{item.getGoodsCode() + StrUtil.COLON + item.getSize()}));
                            continue;
                        } else {
                            // 校验指令单欠数
                            BigDecimal oweQuantity = sizeFinal.getOweQuantity().add(billSize.getQuantity().negate());
                            sizeFinal.setOweQuantity(oweQuantity);
                            if (oweQuantity.compareTo(BigDecimal.ZERO) < 0) {
                                messageList.add(getMessageByParams("billGoodsGtOweQuantity", new Object[]{item.getGoodsCode() + StrUtil.COLON + item.getSize()}));
                                continue;
                            }
                            updateSizeList.add(sizeFinal);
                        }
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
    private void convertQueryContext(SendBillQueryParam param, SendBillQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, SendBillQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, SendBillQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setModuleId(param.getModuleId());
        context.setManualId(param.getManualId());
        context.setBillNo(param.getBillNo());
        context.setNotes(param.getNotes());
        context.setStatus(param.getStatus());
        context.setFields(param.getFields());

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
    private QueryWrapper processQueryWrapper(SendBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<SendBill>();

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
    private List<SendBillQueryResult> convertQueryResult(List<SendBill> list) {
        List<SendBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(SendBill::getId).distinct().collect(Collectors.toList());
        // 发货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, SendBill::getChannelId);
        channelIds.addAll(StreamUtil.toSet(list, SendBill::getToChannelId));
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, SendBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, SendBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<SendBillSize> sendBillSizeList = sendBillSizeDao.selectList(new LambdaQueryWrapper<SendBillSize>().in(SendBillSize::getBillId, billIdList));
        List<SendBillGoods> sendBillGoodsList = sendBillGoodsDao.selectList(new LambdaQueryWrapper<SendBillGoods>().in(SendBillGoods::getBillId, billIdList));
        // 价格类型
        Map<Object, IdNameDto> priceTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<PriceType>().in("id", StreamUtil.toSet(sendBillGoodsList, SendBillGoods::getPriceTypeId)), PriceType.class, LanguageTableEnum.PRICETYPE);
        // 根据单据分组
        Map<Long, List<SendBillSize>> billSizeMap = sendBillSizeList.stream().collect(Collectors.groupingBy(SendBillSize::getBillId));
        Map<Long, List<SendBillGoods>> billGoodsMap = sendBillGoodsList.stream().collect(Collectors.groupingBy(SendBillGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(sendBillGoodsList, SendBillGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(sendBillSizeList, SendBillSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(sendBillSizeList, SendBillSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(sendBillSizeList, SendBillSize::getSizeId)));
        Map<Long, String> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeDetail::getId, SizeDetail::getName));
        // 条码,默认取第一个
        List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>()
                .in(CollUtil.isNotEmpty(goodsMap.keySet()), "goods_id", goodsMap.keySet())
                .in(CollUtil.isNotEmpty(colorMap.keySet()), "color_id", colorMap.keySet())
                .in(CollUtil.isNotEmpty(longMap.keySet()), "long_id", longMap.keySet())
                .in(CollUtil.isNotEmpty(sizeMap.keySet()), "size_id", sizeMap.keySet())
                .orderByDesc("barcode").groupBy("goods_id,color_id,long_id,size_id"));
        Map<String, String> barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getSingleCode, Barcode::getBarcode));
        // 指令单
        Map<Long, String> noticeNoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(StreamUtil.toSet(sendBillGoodsList, SendBillGoods::getNoticeId))) {
            List<NoticeBill> noticeBillList = noticeBillDao.selectList(new LambdaQueryWrapper<NoticeBill>().in(NoticeBill::getId, StreamUtil.toSet(sendBillGoodsList, SendBillGoods::getNoticeId)));
            noticeNoMap = noticeBillList.stream().collect(Collectors.toMap(NoticeBill::getId, NoticeBill::getBillNo));
        }
        // 物流公司
        Map<Long, SendBillLogistics> logisticsMap = new HashMap<>();
        Map<Long, String> logisticsCompanyMap = new HashMap<>();
        List<SendBillLogistics> logisticsList = sendBillLogisticsDao.selectBatchIds(billIdList);
        if (CollUtil.isNotEmpty(logisticsList)) {
            logisticsMap = logisticsList.stream().collect(Collectors.toMap(SendBillLogistics::getBillId, Function.identity()));
            List<Object> ids = logisticsList.stream().map(SendBillLogistics::getLogisticsCompanyId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(ids)) {
                List<Map<Object, Object>> mapList = baseDbDao.getListMap(String.format("select id,code from rbp_logistics_company where status=100 and id in %s", AppendSqlUtil.getInSql(ids)));
                mapList.forEach(item -> logisticsCompanyMap.put((Long) item.get("id"), (String) item.get("code")));
            }
        }
        // 模块自定义字段定义
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(StreamUtil.toList(list, SendBill::getModuleId));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.SEND_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.SEND_BILL_GOODS, StreamUtil.toList(sendBillGoodsList, SendBillGoods::getId));
        // 填充
        for (SendBill bill : list) {
            SendBillQueryResult queryResult = new SendBillQueryResult();
            queryResults.add(queryResult);

            queryResult.setModuleId(bill.getModuleId());
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getChannelId()), IdNameCodeDto::getCode));
            queryResult.setToChannelCode(OptionalUtil.ofNullable(channelMap.get(bill.getToChannelId()), IdNameCodeDto::getCode));
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
            // 物流信息
            SendBillLogistics logistics = logisticsMap.get(bill.getId());
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
            List<SendBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<SendBillGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, SendBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(SendBillGoods::getId, Function.identity()));
            // 尺码明细
            List<SendBillSize> billSizeList = billSizeMap.get(bill.getId());
            for (SendBillSize size : billSizeList) {
                SendBillGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                SendBillGoodsDetailData detailData = new SendBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(billGoods.getId())));
                goodsQueryResultList.add(detailData);

                detailData.setPriceType(OptionalUtil.ofNullable(priceTypeMap.get(billGoods.getPriceTypeId()), IdNameDto::getName));
                detailData.setNoticeNo(noticeNoMap.get(billGoods.getNoticeId()));
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
    private void balanceSetting(SendBillSaveContext context) {
        // 单据主体
        BalanceDetailSampleDto sampleDto = new BalanceDetailSampleDto();
        SendBill bill = context.getBill();
        sampleDto.setModuleId(bill.getModuleId());
        sampleDto.setBillId(bill.getId());
        sampleDto.setBillNo(bill.getBillNo());
        sampleDto.setManualId(bill.getManualId());
        sampleDto.setBillDate(bill.getBillDate());
        sampleDto.setBusinessTypeId(bill.getBusinessTypeId());
        sampleDto.setChannelId(bill.getChannelId());
        sampleDto.setToChannelId(bill.getToChannelId());
        sampleDto.setNotes(bill.getNotes());
        // 货品明细
        List<SendBillGoods> billGoodsList = context.getBillGoodsList();
        List<BalanceDetailSampleDto> goodsList = new ArrayList<>();
        for (SendBillGoods goods : billGoodsList) {
            BalanceDetailSampleDto dto = new BalanceDetailSampleDto();
            goodsList.add(dto);
            dto.setBillGoodsId(goods.getId());
            dto.setPriceTypeId(goods.getPriceTypeId());
            dto.setGoodsId(goods.getGoodsId());
            dto.setQuantity(goods.getQuantity());
            dto.setTagPrice(goods.getTagPrice());
            dto.setTagAmount(NumberUtil.mul(goods.getQuantity(), goods.getTagPrice()));
            dto.setPrice(goods.getBalancePrice());
            dto.setAmount(NumberUtil.mul(goods.getQuantity(), goods.getBalancePrice()));
            dto.setDiscount(goods.getDiscount());
        }
        // 核算
        if (StatusEnum.CHECK.getStatus().equals(bill.getStatus())) {
            systemCommonService.insertGenerateReceivableBalanceDetail(sampleDto, goodsList, true);
        }
        // 临时核算
        systemCommonService.insertGenerateReceivableBalanceTempDetail(sampleDto, goodsList, true);

    }

    /**
     * 库存调整
     *
     * @param context
     */
    private void updateStock(SendBillSaveContext context) {
        SendBill sendBill = context.getBill();

        List<SendBillGoods> sendBillGoodsList = context.getBillGoodsList();
        List<SendBillSize> sendBillSizeList = context.getBillSizeList();
        Long billId = sendBill.getId();
        String billNo = sendBill.getBillNo();
        String moduleId = sendBill.getModuleId();
        Long channelId = sendBill.getChannelId();

        if (sendBillGoodsList.size() == 0) {
            return;
        }

        //按指令发货 不需要处理库存
        SendBillReferenceType sendBillReferenceType = getSendBillReferenceType(sendBill);
        if ((SendBillReferenceType.NOTICE == sendBillReferenceType || SendBillReferenceType.SEND_PLAN == sendBillReferenceType)) {
            return;
        }

        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(channelId);

        //无指令发货 按单据明细增加可用库存占用数
        List<UsableStockLockDetail> usableStockLockDetails = getUsableStockLockDetailsBySave(moduleId, billId, billNo, channelId, sendBillGoodsList, sendBillSizeList, false, true);
        if (usableStockLockDetails.size() > 0) {
            usableStockLockDetailService.insertUsableStockLockDetailList(usableStockLockDetails);
        }

        Set<UsableStockDetail> usableStockDetails = getUsableStockDetails(channelId, sendBillSizeList);
        //无指令发货 (新增-保存 按单据明细减少可用库存。)
        usableStockDetailService.decreaseUsableStockDetail(usableStockDetails, isMustPositive);
    }

    /**
     * 审核单据后更新库存 存在2种场景
     * 1.引用指令单：按单据明细减少实际库存，按单据明细增加可用库存占用数(负数，用发货明细对冲指令单SKU的配货数)，按单据明细增加在途库存。
     * 2.不引用指令单: 按单据明细减少实际库存，清空当前单据的占用数，按单据明细增加在途库存。
     */
    private void checkModifyStock(SendBillSaveContext context) {
        SendBill sendBill = context.getBill();

        Long billId = sendBill.getId();
        String billNo = sendBill.getBillNo();
        String moduleId = sendBill.getModuleId();
        Long channelId = sendBill.getChannelId();
        Long toChannelId = sendBill.getToChannelId();

        List<SendBillSize> sendBillSizes = context.getBillSizeList();
        Map<Long, Long> noticeMap = context.getBillGoodsList().stream().filter(e -> e.getNoticeId() != null).collect(Collectors.toMap(SendBillGoods::getId, SendBillGoods::getNoticeId));
        //是否允许负库存
        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(channelId);

        //清空当前单据的占用数(不引用指令单)
        usableStockLockDetailService.deleteUsableStockLockDetailBySourceBillId(moduleId, billId);
        //插入可用库存占用数(引用单据时，对冲指令单的占用数)
        boolean increaseFlag = false;//减少指令单的占用数
        boolean referenceNoticeId = true;//引用单据
        List<UsableStockLockDetail> usableStockLockDetails = this.getUsableStockLockDetails(moduleId, billId, billNo, channelId, sendBillSizes, noticeMap, referenceNoticeId, increaseFlag);
        if (usableStockLockDetails.size() > 0) {
            usableStockLockDetailService.insertUsableStockLockDetailList(usableStockLockDetails);
        }
        //减少发货方实际库存
        Set<StockDetail> stockDetails = this.getStockDetails(channelId, sendBillSizes);
        stockDetailService.decreaseStockDetail(stockDetails, isMustPositive);

        //增加收货方在途库存
        //0629添加发货业务收货方不产生在途
        boolean sendBusinessReceiverNotBuildForWayStock = systemCommonService.isSendBusinessReceiverNotBuildForWayStock(toChannelId);
        if (!sendBusinessReceiverNotBuildForWayStock) {
            Set<ForwayStockDetail> forwayStockDetails = this.getForwayStockDetails(toChannelId, sendBillSizes);
            forwayStockDetailService.increaseForwayStockDetail(forwayStockDetails, isMustPositive);
        }
    }

    /**
     * 获取关联指令单的需要占用的明细
     *
     * @param sourceModuleId    来源模块
     * @param sourceBillId      来源单据
     * @param sourceBillNo      来源单号
     * @param channelId         渠道
     * @param sendBillSizeList  发货明细
     * @param referenceNoticeId 引用标记：true/只获取引用指令单的明细；false/只获取未引用指令单的明细；
     * @param increaseFlag      增加标记：true/增加; false/减少；
     * @return
     */
    private List<UsableStockLockDetail> getUsableStockLockDetails(String sourceModuleId, Long sourceBillId, String sourceBillNo,
                                                                  Long channelId, List<SendBillSize> sendBillSizeList, Map<Long, Long> noticeMap,
                                                                  boolean referenceNoticeId, boolean increaseFlag) {
        List<UsableStockLockDetail> usableStockLockDetails = new ArrayList<>(sendBillSizeList.size());
        for (SendBillSize sendBillSize : sendBillSizeList) {
            Long noticeId = noticeMap.get(sendBillSize.getBillGoodsId());
            //只获取引用指令单的明细的情况下,指令单号为空，则跳过该记录
            if (referenceNoticeId && noticeId == null) {
                continue;
            }
            //只获取未引用指令单的明细的情况下,指令单号不为空，则跳过该记录
            if (!referenceNoticeId && noticeId != null) {
                continue;
            }
            UsableStockLockDetail usableStockLockDetail = new UsableStockLockDetail();
            BeanUtils.copyProperties(sendBillSize, usableStockLockDetail);
            usableStockLockDetail.setChannelId(channelId);
            //来源单据是发货单
            usableStockLockDetail.setSourceModuleId(sourceModuleId);
            usableStockLockDetail.setSourceBillId(sourceBillId);
            usableStockLockDetail.setSourceBillNo(sourceBillNo);

            if (noticeId != null) {
                //有指令单发货，查询指令单
                NoticeBill noticeBill = noticeBillDao.selectById(noticeId);
                usableStockLockDetail.setModuleId(noticeBill.getModuleId());
                usableStockLockDetail.setBillId(noticeBill.getId());
                usableStockLockDetail.setBillNo(noticeBill.getBillNo());
            } else {
                usableStockLockDetail.setModuleId(sourceModuleId);
                usableStockLockDetail.setBillId(sourceBillId);
                usableStockLockDetail.setBillNo(sourceBillNo);
            }

            if (increaseFlag) {
                //增加占用数(正数，不引用单据的情况下)
                usableStockLockDetail.setQuantity(sendBillSize.getQuantity());
            } else {
                //减少占用数（负数对冲 指令单的占用数）
                usableStockLockDetail.setQuantity(sendBillSize.getQuantity().negate());
            }
            usableStockLockDetails.add(usableStockLockDetail);
        }
        return usableStockLockDetails;
    }


    /**
     * 获取需要调整的实际库存明细
     *
     * @param channelId
     * @param sendBillSizeList
     * @return
     */
    private Set<StockDetail> getStockDetails(Long channelId, List<SendBillSize> sendBillSizeList) {
        Set<StockDetail> stockDetails = new HashSet<>();
        for (SendBillSize billSize : sendBillSizeList) {
            StockDetail stockDetail = new StockDetail();
            BeanUtils.copyProperties(billSize, stockDetail);
            stockDetail.setChannelId(channelId);
            stockDetail.setReduceQuantity(BigDecimal.ZERO);
            stockDetails.add(stockDetail);
        }
        return stockDetails;
    }

    /**
     * 获取需要调整的在途库存明细
     *
     * @param channelId
     * @param sendBillSizeList
     * @return
     */
    private Set<ForwayStockDetail> getForwayStockDetails(Long channelId, List<SendBillSize> sendBillSizeList) {
        Set<ForwayStockDetail> stockDetails = new HashSet<>();
        for (SendBillSize billSize : sendBillSizeList) {
            ForwayStockDetail stockDetail = new ForwayStockDetail();
            BeanUtils.copyProperties(billSize, stockDetail);
            stockDetail.setChannelId(channelId);
            stockDetail.setReduceQuantity(BigDecimal.ZERO);
            stockDetails.add(stockDetail);
        }
        return stockDetails;
    }

    /**
     * 获取发货单需要占用的明细
     *
     * @param sourceModuleId
     * @param sourceBillId
     * @param sourceBillNo
     * @param channelId
     * @param sendBillGoodsList
     * @param sendBillSizeList
     * @param referenceNoticeId
     * @param increaseFlag
     * @return
     */
    private List<UsableStockLockDetail> getUsableStockLockDetailsBySave(String sourceModuleId, Long sourceBillId, String sourceBillNo, Long channelId,
                                                                        List<SendBillGoods> sendBillGoodsList, List<SendBillSize> sendBillSizeList,
                                                                        boolean referenceNoticeId, boolean increaseFlag) {
        List<UsableStockLockDetail> usableStockLockDetails = new ArrayList<>();
        Hashtable<Long, SendBillGoods> hashSendBillGoods = new Hashtable<>(sendBillGoodsList.size());
        for (SendBillGoods sendBillGoods : sendBillGoodsList) {
            //只获取引用指令单的明细的情况下,指令单号为空，则跳过该记录
            if (referenceNoticeId && sendBillGoods.getNoticeId() == null) {
                continue;
            }
            //只获取未引用指令单的明细的情况下,指令单号不为空，则跳过该记录
            if (!referenceNoticeId && sendBillGoods.getNoticeId() != null) {
                continue;
            }
            hashSendBillGoods.put(sendBillGoods.getId(), sendBillGoods);
        }
        for (SendBillSize sendBillSize : sendBillSizeList) {
            Long billGoodsId = sendBillSize.getBillGoodsId();
            if (hashSendBillGoods.containsKey(billGoodsId)) {
                SendBillGoods sendBillGoods = hashSendBillGoods.get(billGoodsId);
                Long noticeId = sendBillGoods.getNoticeId();

                UsableStockLockDetail usableStockLockDetail = new UsableStockLockDetail();
                BeanUtils.copyProperties(sendBillSize, usableStockLockDetail);
                usableStockLockDetail.setChannelId(channelId);
                //来源单据是发货单
                usableStockLockDetail.setSourceModuleId(sourceModuleId);
                usableStockLockDetail.setSourceBillId(sourceBillId);
                usableStockLockDetail.setSourceBillNo(sourceBillNo);
                //有指令单发货，查询指令单
                if (noticeId != null) {
                    NoticeBill noticeBill = noticeBillDao.selectById(noticeId);
                    usableStockLockDetail.setModuleId(noticeBill.getModuleId());
                    usableStockLockDetail.setBillId(noticeBill.getId());
                    usableStockLockDetail.setBillNo(noticeBill.getBillNo());
                } else {
                    usableStockLockDetail.setModuleId(sourceModuleId);
                    usableStockLockDetail.setBillId(sourceBillId);
                    usableStockLockDetail.setBillNo(sourceBillNo);
                }

                if (increaseFlag) {
                    //增加占用数(正数，不引用单据的情况下)
                    usableStockLockDetail.setQuantity(sendBillSize.getQuantity());
                } else {
                    //减少占用数（负数对冲 指令单的占用数）
                    usableStockLockDetail.setQuantity(sendBillSize.getQuantity().negate());
                }
                usableStockLockDetails.add(usableStockLockDetail);
            }
        }
        return usableStockLockDetails;
    }

    /**
     * 获取需要调整的可用库存明细
     *
     * @param channelId
     * @param sendBillSizeList
     * @return
     */
    private Set<UsableStockDetail> getUsableStockDetails(Long channelId, List<SendBillSize> sendBillSizeList) {
        Set<UsableStockDetail> stockDetails = new HashSet<>();
        for (SendBillSize billSize : sendBillSizeList) {
            UsableStockDetail stockDetail = new UsableStockDetail();
            BeanUtils.copyProperties(billSize, stockDetail);
            stockDetail.setChannelId(channelId);
            stockDetail.setReduceQuantity(BigDecimal.ZERO);
            stockDetails.add(stockDetail);
        }
        return stockDetails;
    }

    /**
     * 获取发货单引用单据类型
     *
     * @param sendBill
     * @return
     */
    private SendBillReferenceType getSendBillReferenceType(SendBill sendBill) {
        SendBillReferenceType sendBillReferenceType = SendBillReferenceType.NONE;
        if (null != sendBill.getSendPlanId()) {
            sendBillReferenceType = SendBillReferenceType.SEND_PLAN;
        }
        return sendBillReferenceType;
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
