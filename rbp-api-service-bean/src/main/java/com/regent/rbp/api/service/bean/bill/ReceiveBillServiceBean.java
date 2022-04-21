package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.BusinessType;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.CurrencyType;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.PriceType;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.channel.ChannelSettingValue;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.receiveBill.ReceiveBill;
import com.regent.rbp.api.core.receiveBill.ReceiveBillDifference;
import com.regent.rbp.api.core.receiveBill.ReceiveBillGoods;
import com.regent.rbp.api.core.receiveBill.ReceiveBillRealGoods;
import com.regent.rbp.api.core.receiveBill.ReceiveBillRealSize;
import com.regent.rbp.api.core.receiveBill.ReceiveBillSize;
import com.regent.rbp.api.core.receiveBill.StockBalanceBill;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.core.sendBill.SendBillSize;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.channel.ChannelSettingValueDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillGoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillSizeDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillDifferenceDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillGoodsDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillRealGoodsDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillRealSizeDao;
import com.regent.rbp.api.dao.receiveBill.ReceiveBillSizeDao;
import com.regent.rbp.api.dao.receiveBill.StockBalanceBillDao;
import com.regent.rbp.api.dao.sendBill.SendBillDao;
import com.regent.rbp.api.dao.sendBill.SendBillGoodsDao;
import com.regent.rbp.api.dao.sendBill.SendBillSizeDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillGoodsDetailData;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.dto.receive.ReceiveBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.BillConstants;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.receive.ReceiveBillService;
import com.regent.rbp.api.service.receive.context.ReceiveBillQueryContext;
import com.regent.rbp.api.service.receive.context.ReceiveBillSaveContext;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.model.stock.entity.ForwayStockDetail;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.common.service.stock.ForwayStockDetailService;
import com.regent.rbp.common.service.stock.StockDetailService;
import com.regent.rbp.common.service.stock.UsableStockDetailService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.ChannelSettingEnum;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.regent.rbp.api.core.constants.BusinessTypeConstants.RECEIVE_RETURN_GOODS;
import static com.regent.rbp.api.core.constants.BusinessTypeConstants.RETURN_GOODS;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Service
public class ReceiveBillServiceBean implements ReceiveBillService {
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private DbService dbService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private SendBillDao sendBillDao;
    @Autowired
    private SendBillGoodsDao sendBillGoodsDao;
    @Autowired
    private SendBillSizeDao sendBillSizeDao;
    @Autowired
    private ReceiveBillGoodsDao receiveBillGoodsDao;
    @Autowired
    private ReceiveBillSizeDao receiveBillSizeDao;
    @Autowired
    private ReceiveBillRealGoodsDao receiveBillRealGoodsDao;
    @Autowired
    private ReceiveBillRealSizeDao receiveBillRealSizeDao;
    @Autowired
    private StockBalanceBillDao stockBalanceBillDao;
    @Autowired
    private NoticeBillDao noticeBillDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private ReceiveBillDifferenceDao receiveBillDifferenceDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private NoticeBillGoodsDao noticeBillGoodsDao;
    @Autowired
    private NoticeBillSizeDao noticeBillSizeDao;
    @Autowired
    private ChannelSettingValueDao channelSettingValueDao;
    @Autowired
    private StockDetailService stockDetailService;
    @Autowired
    private UsableStockDetailService usableStockDetailService;
    @Autowired
    private ForwayStockDetailService forwayStockDetailService;

    @Override
    public PageDataResponse<ReceiveBillQueryResult> query(ReceiveBillQueryParam param) {
        ReceiveBillQueryContext context = new ReceiveBillQueryContext();
        convertQueryContext(param, context);
        PageDataResponse<ReceiveBillQueryResult> result = new PageDataResponse<>();
        if (!context.isValid()) {
            result.setData(Collections.emptyList());
            result.setTotalCount(0);
            return result;
        }
        Page<ReceiveBill> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = processQueryWrapper(context);
        IPage<ReceiveBill> receiveBillIPage = receiveBillDao.selectPage(pageModel, queryWrapper);
        List<ReceiveBillQueryResult> list = convertQueryResult(receiveBillIPage.getRecords());
        result.setTotalCount(receiveBillIPage.getTotal());
        result.setData(list);
        return result;
    }

    private List<ReceiveBillQueryResult> convertQueryResult(List<ReceiveBill> list) {
        List<ReceiveBillQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> billIdList = list.stream().map(ReceiveBill::getId).distinct().collect(Collectors.toList());
        // 发货渠道
        Set<Long> channelIds = StreamUtil.toSet(list, ReceiveBill::getChannelId);
        channelIds.addAll(StreamUtil.toSet(list, ReceiveBill::getToChannelId));
        Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", channelIds), Channel.class, LanguageTableEnum.CHANNEL);
        // 业务类型
        Map<Object, IdNameDto> businessTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<BusinessType>().in("id", StreamUtil.toSet(list, ReceiveBill::getBusinessTypeId)), BusinessType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 币种类型
        Map<Object, IdNameDto> currencyTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<CurrencyType>().in("id", StreamUtil.toSet(list, ReceiveBill::getCurrencyTypeId)), CurrencyType.class, LanguageTableEnum.BUSINESS_TYPE);
        // 货品尺码明细
        List<ReceiveBillRealSize> receiveBillSizes = receiveBillRealSizeDao.selectList(new LambdaQueryWrapper<ReceiveBillRealSize>().in(ReceiveBillRealSize::getBillId, billIdList));
        List<ReceiveBillRealGoods> receiveBillRealGoods = receiveBillRealGoodsDao.selectList(new LambdaQueryWrapper<ReceiveBillRealGoods>().in(ReceiveBillRealGoods::getBillId, billIdList));
        // 价格类型
        Map<Object, IdNameDto> priceTypeMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<PriceType>().in("id", StreamUtil.toSet(receiveBillRealGoods, ReceiveBillRealGoods::getPriceTypeId)), PriceType.class, LanguageTableEnum.PRICETYPE);
        // 根据单据分组
        Map<Long, List<ReceiveBillRealSize>> billSizeMap = receiveBillSizes.stream().collect(Collectors.groupingBy(ReceiveBillRealSize::getBillId));
        Map<Long, List<ReceiveBillRealGoods>> billGoodsMap = receiveBillRealGoods.stream().collect(Collectors.groupingBy(ReceiveBillRealGoods::getBillId));
        // 货品
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, StreamUtil.toSet(receiveBillRealGoods, ReceiveBillRealGoods::getGoodsId)));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        // 颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, StreamUtil.toSet(receiveBillSizes, ReceiveBillRealSize::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode));
        // 内长
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, StreamUtil.toSet(receiveBillSizes, ReceiveBillRealSize::getLongId)));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));
        // 尺码
        List<SizeDetail> sizeList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>().in(SizeDetail::getId, StreamUtil.toSet(receiveBillSizes, ReceiveBillRealSize::getSizeId)));
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
        List<Long> noticeIds = CollUtil.map(list, ReceiveBill::getNoticeId, true);
        if (CollUtil.isNotEmpty(noticeIds)) {
            List<NoticeBill> noticeBillList = noticeBillDao.selectList(new LambdaQueryWrapper<NoticeBill>().in(NoticeBill::getId, noticeIds));
            noticeNoMap = noticeBillList.stream().collect(Collectors.toMap(NoticeBill::getId, NoticeBill::getBillNo));
        }
        // 发货单
        Map<Long, String> sendBillIdNoMap = new HashMap<>();
        List<Long> sendBillIds = CollUtil.distinct(CollUtil.map(list, ReceiveBill::getSendId, true));
        if (CollUtil.isNotEmpty(sendBillIds)) {
            sendBillIdNoMap = sendBillDao.selectList(new LambdaQueryWrapper<SendBill>().in(SendBill::getId, sendBillIds)).stream().collect(Collectors.toMap(SendBill::getId, SendBill::getBillNo));
        }
        // 模块自定义字段定义
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(list, ReceiveBill::getModuleId, true));
        // 单据自定义字段
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.RECEIVE_BILL, billIdList);
        // 货品自定义字段
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.RECEIVE_BILL_REAL_GOODS, CollUtil.map(receiveBillRealGoods, ReceiveBillRealGoods::getId, true));
        // 填充
        for (ReceiveBill bill : list) {
            ReceiveBillQueryResult queryResult = new ReceiveBillQueryResult();
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
            queryResult.setNoticeNo(noticeNoMap.get(bill.getNoticeId()));
            queryResult.setSendNo(sendBillIdNoMap.get(bill.getSendId()));
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(bill.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            queryResult.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(bill.getId())));
            // 货品列表
            List<ReceiveBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            queryResult.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<ReceiveBillRealGoods> billGoodsList = billGoodsMap.get(bill.getId());
            Map<Long, ReceiveBillRealGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(ReceiveBillRealGoods::getId, Function.identity()));
            // 尺码明细
            List<ReceiveBillRealSize> billSizeList = billSizeMap.get(bill.getId());
            for (ReceiveBillRealSize size : billSizeList) {
                ReceiveBillRealGoods billGoods = currentGoodsMap.get(size.getBillGoodsId());
                ReceiveBillGoodsDetailData detailData = new ReceiveBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(billGoods.getId())));
                goodsQueryResultList.add(detailData);

                detailData.setPriceType(OptionalUtil.ofNullable(priceTypeMap.get(billGoods.getPriceTypeId()), IdNameDto::getName));
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

    private QueryWrapper processQueryWrapper(ReceiveBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<ReceiveBill>();

        queryWrapper.eq(StringUtil.isNotEmpty(context.getModuleId()), "module_id", context.getModuleId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getManualId()), "manual_id", context.getManualId());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getBillNo()), "bill_no", context.getBillNo());
        queryWrapper.in(CollUtil.isNotEmpty(context.getStatus()), "status", context.getStatus());
        if (null != context.getBusinessTypeIds() && !context.getBusinessTypeIds().isEmpty()) {
            queryWrapper.in("business_type_id", context.getBusinessTypeIds());
        }
        if (null != context.getCurrencyTypeIds() && CollUtil.isNotEmpty(context.getCurrencyTypeIds())) {
            queryWrapper.in("currency_type_id", context.getCurrencyTypeIds());
        }
        if (null != context.getChannelIds() && CollUtil.isNotEmpty(context.getChannelIds())) {
            queryWrapper.in("channel_id", context.getChannelIds());
        }
        if (null != context.getToChannelIds() && CollUtil.isNotEmpty(context.getToChannelIds())) {
            queryWrapper.in("to_channel_id", context.getToChannelIds());
        }

        queryWrapper.eq(null != context.getBillDate(), "bill_date", context.getBillDate());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());
        queryWrapper.ge(null != context.getCheckDateStart(), "check_time", context.getCheckDateStart());
        queryWrapper.le(null != context.getCheckDateEnd(), "check_time", context.getCheckDateEnd());
        queryWrapper.like(StrUtil.isNotEmpty(context.getNotes()), "notes", context.getNotes());
        queryWrapper.eq(null != context.getSendId(), "send_id", context.getSendId());
        queryWrapper.eq(null != context.getNoticeId(), "notice_id", context.getNoticeId());

        return queryWrapper;

    }

    private void convertQueryContext(ReceiveBillQueryParam param, ReceiveBillQueryContext context) {
        context.setPageNo(OptionalUtil.ofNullable(param, ReceiveBillQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, ReceiveBillQueryParam::getPageSize, SystemConstants.PAGE_SIZE));

        context.setModuleId(param.getModuleId());
        context.setManualId(param.getManualId());
        context.setBillNo(param.getBillNo());
        context.setBillDate(param.getBillDate());
        context.setNotes(param.getNotes());
        context.setStatus(param.getStatus());
        context.setFields(param.getFields());

        // 收货渠道
        if (CollUtil.isNotEmpty(param.getChannelCode())) {
            List<Channel> idList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getChannelCode()));
            context.setChannelIds(CollUtil.map(idList, Channel::getId, true));
        }
        // 发货渠道
        if (CollUtil.isNotEmpty(param.getToChannelCode())) {
            List<Channel> idList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getToChannelCode()));
            context.setToChannelIds(CollUtil.map(idList, Channel::getId, true));
        }
        // 业务类型
        if (CollUtil.isNotEmpty(param.getBusinessType())) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_business_type where status=100 %s", AppendSqlUtil.getInStrSql("name", param.getBusinessType())));
            context.setBusinessTypeIds(idList);
        }
        // 币种类型
        if (CollUtil.isNotEmpty(param.getCurrencyType())) {
            List<Long> idList = baseDbDao.getLongListDataBySql(String.format("select id from rbp_currency_type where status=100  %s", AppendSqlUtil.getInStrSql("name", param.getCurrencyType())));
            context.setCurrencyTypeIds(idList);
        }
        if (StrUtil.isNotEmpty(param.getSendNo())) {
            SendBill sendBill = sendBillDao.selectOne(new QueryWrapper<SendBill>().eq("bill_no", param.getSendNo()));
            context.setSendId(sendBill.getId());
        }
        if (StrUtil.isNotEmpty(param.getNoticeNo())) {
            NoticeBill noticeBill = noticeBillDao.selectOne(new QueryWrapper<NoticeBill>().eq("bill_no", param.getNoticeNo()));
            context.setNoticeId(noticeBill.getId());
        }

    }

    /**
     * sendNo允许为空
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public DataResponse save(ReceiveBillSaveParam param) {
        List<String> messageList = new ArrayList<>();
        ReceiveBillSaveContext context = new ReceiveBillSaveContext();
        convertSaveContext(context, param, messageList);
        if (!messageList.isEmpty()) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList.stream().distinct().collect(Collectors.toList()))}));
        }
        context.getBillRealGoodsList().forEach(receiveBillRealGoodsDao::insert);
        context.getBillRealSizeList().forEach(receiveBillRealSizeDao::insert);
        if (StrUtil.isEmpty(param.getSendNo()) && StrUtil.isEmpty(param.getNoticeNo())) {
            // 实收作为原单数目，不可能存在差异
            context.getBillRealGoodsList().stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillGoods.class)).forEach(receiveBillGoodsDao::insert);
            context.getBillRealSizeList().stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillSize.class)).forEach(receiveBillSizeDao::insert);
        } else {
            context.getBillGoodsList().stream().forEach(receiveBillGoodsDao::insert);
            context.getBillSizeList().stream().forEach(receiveBillSizeDao::insert);
        }
        receiveBillDao.insert(context.getBill());
        handleReceiveBillDiff(context.getBill().getId());
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.RECEIVE_BILL, context.getBill().getId(), context.getBillCustomizeDataDtos());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.RECEIVE_BILL_REAL_GOODS, context.getReceiveGoodsCustomizeData());
        if (param.getStatus()== StatusEnum.CHECK.getStatus().intValue()){
            checkModifyStock(context.getBill().getId());
        }

        return DataResponse.success();
    }

    public boolean isAllowNegativeInventory(Long channelId) {
        String keyCode = ChannelSettingEnum.ALLOW_NEGATIVE_INVENTORY.getCode(); //允许负库存
        boolean value = Boolean.parseBoolean(ChannelSettingEnum.ALLOW_NEGATIVE_INVENTORY.getDefaultValue());
        ChannelSettingValue  channelSettingValue = channelSettingValueDao.selectOne(new QueryWrapper<com.regent.rbp.api.core.channel.ChannelSettingValue>().eq("key_code", keyCode).eq("channel_id", channelId));
        if (null != channelSettingValue) {
            value = Boolean.parseBoolean(channelSettingValue.getValue());
        }
        return value;
    }

    private void checkModifyStock(Long billId) {
        //发货方
        ReceiveBill receiveBill = receiveBillDao.selectById(billId);
        //收货方
        Long toChannelId = receiveBillDao.selectById(billId).getToChannelId();
        List<ReceiveBillRealSize> billRealSizeList = receiveBillRealSizeDao.selectList(new QueryWrapper<ReceiveBillRealSize>().eq("bill_id", billId));
        //是否允许负库存
        boolean isMustPositive = !isAllowNegativeInventory(toChannelId);
        Set<StockDetail> stockDetailSet = new HashSet<>();
        Set<UsableStockDetail> usableStockDetailSet = new HashSet<>();
        Set<ForwayStockDetail> forwayStockDetailSet = new HashSet<>();

        for (ReceiveBillRealSize billRealSize : billRealSizeList) {
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

        // 不引用单据，不需要操作在途库存
        if (null != receiveBill.getSendId()) {
            /*在途应该拿原单进行加减在途库存*/
            List<ReceiveBillSize> billSizeList = receiveBillSizeDao.selectList(new QueryWrapper<ReceiveBillSize>().eq("bill_id", billId));
            for (ReceiveBillSize billSize : billSizeList) {
                //3.减少收方在途库存
                ForwayStockDetail forwayStockDetail = new ForwayStockDetail();
                BeanUtil.copyProperties(billSize, forwayStockDetail);
                forwayStockDetail.setChannelId(toChannelId);
                forwayStockDetail.setReduceQuantity(billSize.getQuantity().negate());
                forwayStockDetailSet.add(forwayStockDetail);
            }
            forwayStockDetailService.writeForwayStockDetail(forwayStockDetailSet, isMustPositive);
        }
    }

    private void convertSaveContext(ReceiveBillSaveContext context, ReceiveBillSaveParam param, List<String> messageList) {
        /****************   订单主体    ******************/
        ReceiveBill bill = new ReceiveBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        context.setBill(bill);
        context.setBillCustomizeDataDtos(param.getCustomizeData());
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        bill.setBillNo(systemCommonService.getBillNo(param.getModuleId()));
        bill.setStatus(CheckEnum.NOCHECK.getStatus());
        bill.setProcessStatus(0);
        // 业务类型
        if (StringUtil.isNotEmpty(param.getBusinessType())) {
            bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
            Long baseBusinessTypeId = baseDbDao.getLongDataBySql(String.format("select base_business_type_id from rbp_business_type where name = '%s'", param.getBusinessType()));
            context.setBaseBusinessTypeId(baseBusinessTypeId);
            param.setBaseBusinessTypeId(baseBusinessTypeId);
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
        // 订单主体校验
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        // 货品实收明细
        extractedRealGoodsAndSizes(context, param, messageList, bill);
        // 货品原单明细
        if (StrUtil.isNotEmpty(param.getSendNo())) {
            extractedOriginGoodsAndSizesFromSendBill(context, param, messageList, bill);
        } else if (StrUtil.isNotEmpty(param.getNoticeNo())) {
            Boolean openRef = baseDbDao.getBooleanDataBySql(String.format("select value from rbp_channel_setting_value where channel_id  = '%s' and key_code  = '%s'", context.getBill().getChannelId(), ChannelSettingEnum.RECEIVE_RETURN_BUSINESS_ALLOW_REFERENCE_NOTICEBILL));
            if (!openRef) {
                messageList.add("发货渠道不允许引用指令单");
                return;
            }
            extractedOriginGoodsAndSizesFromNoticeBill(context, param, messageList, bill);
        }

        StockBalanceBill stockBalanceBill = stockBalanceBillDao.selectOne(new LambdaQueryWrapper<StockBalanceBill>()
                .eq(StockBalanceBill::getChannelId, context.getBill().getToChannelId()).orderByDesc(StockBalanceBill::getBillDate).last("LIMIT 1"));
        if (null != stockBalanceBill && stockBalanceBill.getBillDate().compareTo(context.getBill().getBillDate()) >= 0) {
            messageList.add(String.format("StockBalance已经存在"));
        }

    }

    private void extractedOriginGoodsAndSizesFromNoticeBill(ReceiveBillSaveContext context, ReceiveBillSaveParam param, List<String> messageList, ReceiveBill bill) {
        NoticeBill noticeBill = noticeBillDao.selectOne(new QueryWrapper<NoticeBill>().eq("bill_no", param.getNoticeNo()));
        if (noticeBill == null) {
            messageList.add(getNotExistMessage("noticeNoNotExist"));
            return;
        } else {
            context.getBill().setNoticeId(noticeBill.getId());
            if (noticeBill.getStatus() != 1) {
                messageList.add(getNotExistMessage("sendNoPathNotCheck"));
            }
            if (noticeBill.getChannelId().longValue() != context.getBill().getChannelId() | noticeBill.getToChannelId().longValue() != context.getBill().getToChannelId()) {
                messageList.add(getNotExistMessage("sendNoPathNotMatch"));
            }
            if (!messageList.isEmpty())
                return;
        }
        List<ReceiveBillGoods> receiveBillGoods = noticeBillGoodsDao.selectList(new QueryWrapper<NoticeBillGoods>().eq("bill_id", noticeBill.getId())).stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillGoods.class)).collect(Collectors.toList());
        List<ReceiveBillSize> receiveBillSizes = noticeBillSizeDao.selectList(new QueryWrapper<NoticeBillSize>().eq("bill_id", noticeBill.getId())).stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillSize.class)).collect(Collectors.toList());
        Map<Long, ReceiveBillGoods> receiveBillGoodsMap = receiveBillGoods.stream().collect(Collectors.toMap(ReceiveBillGoods::getId, Function.identity()));
        Map<Long, List<ReceiveBillSize>> grbs = receiveBillSizes.stream().collect(Collectors.groupingBy(ReceiveBillSize::getBillGoodsId));
        grbs.forEach((billGoodsId, vs) -> {
            ReceiveBillGoods goods = receiveBillGoodsMap.get(billGoodsId);
            goods.setPriceTypeId(noticeBill.getPriceTypeId());
            goods.setId(systemCommonService.getId());
            goods.setBillId(context.getBill().getId());
            vs.forEach(v -> {
                v.setBillGoodsId(goods.getId());
                v.setBillId(context.getBill().getId());
                v.setId(systemCommonService.getId());
            });

        });
        context.setBillGoodsList(receiveBillGoods);
        context.setBillSizeList(receiveBillSizes);
    }

    private void extractedOriginGoodsAndSizesFromSendBill(ReceiveBillSaveContext context, ReceiveBillSaveParam param, List<String> messageList, ReceiveBill bill) {
        SendBill sendBill = sendBillDao.selectOne(new QueryWrapper<SendBill>().eq("bill_no", param.getSendNo()));
        if (sendBill == null) {
            messageList.add(getNotExistMessage("sendNoNotExist"));
            return;
        } else {
            context.getBill().setSendId(sendBill.getId());
            if (context.getBaseBusinessTypeId() == RECEIVE_RETURN_GOODS) {
                Long sendBillBaseBusinessTypeId = baseDbDao.getLongDataBySql(String.format("select base_business_type_id from rbp_business_type where id = '%s'", sendBill.getBusinessTypeId()));
                if (sendBillBaseBusinessTypeId != RETURN_GOODS) {
                    messageList.add(getNotExistMessage("sendNoTypeError"));
                }
            }
            if (sendBill.getStatus() != 1) {
                messageList.add(getNotExistMessage("sendNoPathNotCheck"));
            }
            if (sendBill.getChannelId().longValue() != context.getBill().getChannelId() | sendBill.getToChannelId().longValue() != context.getBill().getToChannelId()) {
                messageList.add(getNotExistMessage("sendNoPathNotMatch"));
            }
            if (!messageList.isEmpty()) {
                return;
            }
        }
        List<ReceiveBillGoods> receiveBillGoods = sendBillGoodsDao.selectList(new QueryWrapper<SendBillGoods>().eq("bill_id", sendBill.getId())).stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillGoods.class)).collect(Collectors.toList());
        List<ReceiveBillSize> receiveBillSizes = sendBillSizeDao.selectList(new QueryWrapper<SendBillSize>().eq("bill_id", sendBill.getId())).stream().map(e -> BeanUtil.copyProperties(e, ReceiveBillSize.class)).collect(Collectors.toList());
        Map<Long, ReceiveBillGoods> receiveBillGoodsMap = receiveBillGoods.stream().collect(Collectors.toMap(ReceiveBillGoods::getId, Function.identity()));
        Map<Long, List<ReceiveBillSize>> grbs = receiveBillSizes.stream().collect(Collectors.groupingBy(ReceiveBillSize::getBillGoodsId));
        grbs.forEach((billGoodsId, vs) -> {
            ReceiveBillGoods goods = receiveBillGoodsMap.get(billGoodsId);
            goods.setId(systemCommonService.getId());
            goods.setBillId(context.getBill().getId());
            vs.forEach(v -> {
                v.setBillGoodsId(goods.getId());
                v.setBillId(context.getBill().getId());
                v.setId(systemCommonService.getId());
            });

        });
        context.setBillGoodsList(receiveBillGoods);
        context.setBillSizeList(receiveBillSizes);
    }

    private void extractedRealGoodsAndSizes(ReceiveBillSaveContext context, ReceiveBillSaveParam param, List<String> messageList, ReceiveBill bill) {
        List<ReceiveBillRealGoods> billGoodsList = new ArrayList<>();
        context.setBillRealGoodsList(billGoodsList);
        List<ReceiveBillRealSize> billSizeList = new ArrayList<>();
        context.setBillRealSizeList(billSizeList);
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
        if (param.getGoodsDetailData().stream().filter(f -> StringUtil.isNotEmpty(f.getBarcode())).count() > 0) {
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
        // 尺码明细
        List<ReceiveBillRealSize> sizeList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (ReceiveBillGoodsDetailData item : param.getGoodsDetailData()) {
            atomicInteger.getAndIncrement();
            ReceiveBillRealSize size = new ReceiveBillRealSize();
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
        for (ReceiveBillGoodsDetailData item : param.getGoodsDetailData()) {
            // TODO 结算价不存在,则通过分销价格获取
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId())) {

            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(atomicInteger.get(), "balancePrice"));
            }
        }
        // 尺码根据行ID分组
        Map<Long, ReceiveBillRealSize> receiveBillSizeMap = sizeList.stream().collect(Collectors.toMap(ReceiveBillRealSize::getId, Function.identity()));

        atomicInteger.set(0);
        // 根据货品+价格分组，支持同款多价
        Map<String, Long> finalPriceTypeMap = priceTypeMap;
        param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
            atomicInteger.getAndIncrement();
            ReceiveBillRealGoods billGoods = ReceiveBillRealGoods.build();
            billGoodsList.add(billGoods);

            billGoods.setBillId(bill.getId());
            ReceiveBillGoodsDetailData detailData = sizes.get(0);
            billGoods.setGoodsId(detailData.getGoodsId());
            billGoods.setTagPrice(detailData.getTagPrice());
            billGoods.setPriceTypeId(finalPriceTypeMap.get(detailData.getPriceType()));
            billGoods.setBalancePrice(detailData.getBalancePrice());
            billGoods.setDiscount(detailData.getDiscount());
            billGoods.setCurrencyPrice(detailData.getCurrencyPrice());
            billGoods.setExchangeRate(detailData.getExchangeRate());
            billGoods.setQuantity(detailData.getQuantity());
            billGoods.setRemark(detailData.getRemark());
            context.addGoodsDetailCustomData(sizes.get(0).getGoodsCustomizeData(), billGoods.getId());
            // 自定义字段
            if (CollUtil.isNotEmpty(detailData.getGoodsCustomizeData())) {
                Map<String, Object> customFieldMap = new HashMap<>();
                customFieldMap.put("id", billGoods.getId());
                detailData.getGoodsCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
                billGoods.setCustomFieldMap(customFieldMap);
            }
            // 尺码明细
            for (ReceiveBillGoodsDetailData item : sizes) {
                ReceiveBillRealSize billSize = receiveBillSizeMap.get(item.getColumnId());
                billSizeList.add(billSize);
                billSize.setBillGoodsId(billGoods.getId());
            }
        });
    }

    private static String getNotExistMessage(String key) {
        return getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotExistMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotExistMessage(key)});
    }

    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }

    public void updateProcessStatus(Long billId) {
        if (ObjectUtil.isNotEmpty(billId)) {
            ReceiveBill receiveBill = receiveBillDao.selectById(billId);
            if (receiveBill != null) {
                //基础处理状态
                Integer oldProcessStatus = receiveBill.getProcessStatus();
                if (null == oldProcessStatus) {
                    oldProcessStatus = -1;
                }
                //0. 无差异  收发没有差异
                //1. 有差异  收发有差异
                //2. 部分处理  差异明细已处理一部分
                //3. 已处理  差异明细已全部处理完

                //由3->0判断, 只要有一个判断成功, 则不往下走
                boolean isDifferenceFlag = false; //默认无差异
                boolean partProcessFlag = false;  //是否部分处理
                boolean allProcessFlag = true; //是否全部处理
                List<ReceiveBillDifference> receiveBillDifferenceList = receiveBillDifferenceDao.selectList(new QueryWrapper<ReceiveBillDifference>().eq("bill_id", billId));
                if (CollUtil.isNotEmpty(receiveBillDifferenceList)) {
                    isDifferenceFlag = true;
                    for (ReceiveBillDifference receiveBillDifference : receiveBillDifferenceList) {
                        if (receiveBillDifference.getProcessStatus() == BillConstants.ReceiveDifferenceProcessStatusConstants.UNPROCESS) {
                            partProcessFlag = true;
                            allProcessFlag = false;
                        }
                    }
                    if (partProcessFlag) {
                        allProcessFlag = false;
                    }
                } else {
                    //无差异
                    isDifferenceFlag = false;
                }
                //有差异, 并且差异全部处理
                if (isDifferenceFlag && allProcessFlag && oldProcessStatus != 3) {
                    receiveBill.setProcessStatus(3);
                    receiveBillDao.updateById(receiveBill);
                    return;
                }
                //有差异, 差异部分处理
                if (isDifferenceFlag && partProcessFlag && oldProcessStatus != 2) {
                    receiveBill.setProcessStatus(2);
                    receiveBillDao.updateById(receiveBill);
                    return;
                }
                //有差异, 差异部分处理
                if (isDifferenceFlag && oldProcessStatus != 1) {
                    receiveBill.setProcessStatus(1);
                    receiveBillDao.updateById(receiveBill);
                    return;
                }
                //没有差异
                if (!isDifferenceFlag && oldProcessStatus != 0) {
                    receiveBill.setProcessStatus(0);
                    receiveBillDao.updateById(receiveBill);
                    return;
                }
            }
        }
    }

    public void handleReceiveBillDiff(Long billId) {
        //原单和实收都存在货品列表
        List<ReceiveBillDifference> receiveBillDifferenceList = new ArrayList<>();
        //原单存在，实收不存在的货品列表
        List<ReceiveBillDifference> orginBillDifferenceList = new ArrayList<>();
        //原单不存在，实收存在的货品列表
        List<ReceiveBillDifference> realBillDifferenceList = new ArrayList<>();
        //收货单尺码实收明细表
        List<ReceiveBillRealSize> receiveBillRealSizeList = receiveBillRealSizeDao.selectList(new QueryWrapper<ReceiveBillRealSize>().eq("bill_id", billId));
        //收货单尺码原单明细表
        List<ReceiveBillSize> receiveBillSizeList = receiveBillSizeDao.selectList(new QueryWrapper<ReceiveBillSize>().eq("bill_id", billId));
        Map<String, ReceiveBillSize> receiveBillSizeMap = new LinkedHashMap<>();
        Map<String, ReceiveBillRealSize> receiveBillRealSizeMap = new LinkedHashMap<>();
        Integer processStatus = InformationConstants.ReceiveDifferenceProcessStatusConstants.UNPROCESS;
        for (ReceiveBillSize receiveBillSize : receiveBillSizeList) {
            receiveBillSizeMap.put(receiveBillSize.getUniqueKey(), receiveBillSize);
        }
        for (ReceiveBillRealSize receiveBillRealSize : receiveBillRealSizeList) {
            ReceiveBillDifference receiveBillDifference = new ReceiveBillDifference();
            receiveBillDifference.setGoodsId(receiveBillRealSize.getGoodsId());
            receiveBillDifference.setColorId(receiveBillRealSize.getColorId());
            receiveBillDifference.setLongId(receiveBillRealSize.getLongId());
            receiveBillDifference.setSizeId(receiveBillRealSize.getSizeId());
            receiveBillDifference.setProcessStatus(processStatus);
            receiveBillDifference.setCreatedTime(new Date());
            receiveBillDifference.setCreatedBy(ThreadLocalGroup.getUserId());
            //sku相同
            if (null != receiveBillSizeMap.get(receiveBillRealSize.getUniqueKey())) {
                //差异数  = 原单数  - 实收数
                receiveBillDifference.setQuantity(NumberUtil.sub(receiveBillSizeMap.get(receiveBillRealSize.getUniqueKey()).getQuantity(), receiveBillRealSize.getQuantity()));
                receiveBillDifferenceList.add(receiveBillDifference);
            } else {
                //获取实收存在，原单不存在的sku,
                //差异数  = 实收数* -1
                receiveBillDifference.setQuantity(receiveBillRealSize.getQuantity().negate());
                realBillDifferenceList.add(receiveBillDifference);
            }
            receiveBillRealSizeMap.put(receiveBillRealSize.getUniqueKey(), receiveBillRealSize);
        }

        for (ReceiveBillSize receiveBillSize : receiveBillSizeList) {
            //获取原单存在，实收不存在的sku
            if (null == receiveBillRealSizeMap.get(receiveBillSize.getUniqueKey())) {
                ReceiveBillDifference receiveBillDifference = new ReceiveBillDifference();
                receiveBillDifference.setGoodsId(receiveBillSize.getGoodsId());
                receiveBillDifference.setColorId(receiveBillSize.getColorId());
                receiveBillDifference.setLongId(receiveBillSize.getLongId());
                receiveBillDifference.setSizeId(receiveBillSize.getSizeId());
                receiveBillDifference.setProcessStatus(processStatus);
                //差异数  = 原单数
                receiveBillDifference.setQuantity(receiveBillSize.getQuantity());
                orginBillDifferenceList.add(receiveBillDifference);
            }
        }
        receiveBillDifferenceList.addAll(orginBillDifferenceList);
        receiveBillDifferenceList.addAll(realBillDifferenceList);

        saveReceiveDifference(billId, receiveBillDifferenceList);
    }

    /**
     * 正常货品按一件一行进行拆分
     * 计量货品不按一件一行进行拆分
     *
     * @param billId
     * @param receiveBillDifferenceList
     */
    private void saveReceiveDifference(Long billId, List<ReceiveBillDifference> receiveBillDifferenceList) {
        if (CollUtil.isEmpty(receiveBillDifferenceList)) {
            return;
        }
        //先删除原来的数据
        receiveBillDifferenceDao.delete(new QueryWrapper<ReceiveBillDifference>().eq("bill_id", billId));
        for (ReceiveBillDifference receiveBillDifference : receiveBillDifferenceList) {
            if (null != receiveBillDifference && receiveBillDifference.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            //计量商品
            Goods goods = goodsDao.selectById(receiveBillDifference.getGoodsId());
            boolean isMetric = false;
            //计量商品标记: 0否 1是
            isMetric = BooleanUtil.isTrue(goods.getMetricFlag());
            BigDecimal quantity = receiveBillDifference.getQuantity();
            Integer forCount = quantity.abs().intValue();
            if (quantity.compareTo(BigDecimal.ZERO) == 1) {
                //差异为正数
                if (isMetric) {
                    receiveBillDifference.setId(systemCommonService.getId());
                    receiveBillDifference.setBillId(billId);
                    receiveBillDifference.setQuantity(receiveBillDifference.getQuantity());
                    receiveBillDifferenceDao.insert(receiveBillDifference);
                } else {
                    for (int i = 0; i < forCount; i++) {
                        receiveBillDifference.setId(systemCommonService.getId());
                        receiveBillDifference.setBillId(billId);
                        receiveBillDifference.setQuantity(BigDecimal.ONE);
                        receiveBillDifferenceDao.insert(receiveBillDifference);
                    }
                }
            } else if (quantity.compareTo(BigDecimal.ZERO) == -1) {
                //差异为负数
                if (isMetric) {
                    receiveBillDifference.setId(systemCommonService.getId());
                    receiveBillDifference.setBillId(billId);
                    receiveBillDifference.setQuantity(receiveBillDifference.getQuantity());
                    receiveBillDifferenceDao.insert(receiveBillDifference);
                } else {
                    for (int i = 0; i < forCount; i++) {
                        receiveBillDifference.setId(systemCommonService.getId());
                        receiveBillDifference.setBillId(billId);
                        receiveBillDifference.setQuantity(BigDecimal.ONE.negate());
                        receiveBillDifferenceDao.insert(receiveBillDifference);
                    }
                }
            }
        }
    }

}


