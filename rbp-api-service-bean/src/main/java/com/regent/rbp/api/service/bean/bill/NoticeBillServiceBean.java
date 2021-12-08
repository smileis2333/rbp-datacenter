package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.noticeBill.NoticeBillLogistics;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.salePlan.SalePlanBillSizeFinal;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillLogisticsDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillSizeFinalDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.notice.NoticeBillBillGoodsDetailData;
import com.regent.rbp.api.dto.notice.NoticeBillQueryParam;
import com.regent.rbp.api.dto.notice.NoticeBillQueryResult;
import com.regent.rbp.api.dto.notice.NoticeBillSaveParam;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.notice.NoticeBillService;
import com.regent.rbp.api.service.notice.context.NoticeBillSaveContext;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private SalePlanBillSizeFinalDao salePlanBillSizeFinalDao;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<NoticeBillQueryResult> query(NoticeBillQueryParam param) {
        return null;
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
        String msg = this.convertSaveContext(context, param);
        if (StringUtil.isNotEmpty(msg)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{msg}));
        }
        NoticeBill bill = context.getBill();
        List<NoticeBillGoods> billGoodsList = context.getBillGoodsList();
        List<NoticeBillSize> billSizeList = context.getBillSizeList();
        NoticeBillLogistics logistics = context.getLogistics();
        // 获取单号
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        // 新增订单
        noticeBillDao.insert(bill);
        // TODO 单据自定义字段
        // 新增物流信息
        noticeBillLogisticsDao.insert(logistics);
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
        return ModelDataResponse.Success(bill.getBillNo());
    }


    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private String convertSaveContext(NoticeBillSaveContext context, NoticeBillSaveParam param) {
        List<String> messageList = new ArrayList<>();
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

        if (StringUtil.isNotEmpty(param.getSalePlanNo())) {
            Long salePlanId = baseDbDao.getLongDataBySql(String.format("select id from rbp_sale_plan_bill where status=1 and bil_no = '%s'", param.getSalePlanNo()));
            if (null == salePlanId) {
                messageList.add(getNotExistMessage("salePlanNo"));
            }
            bill.setSalePlanId(salePlanId);
            List<SalePlanBillSizeFinal> finalList = salePlanBillSizeFinalDao.selectList(new QueryWrapper<SalePlanBillSizeFinal>().eq("bill_id", bill.getSalePlanId()));
            if (CollUtil.isEmpty(finalList)) {
                messageList.add(getNotExistMessage("salePlanBillGoodsList"));
            }

        }
        // 业务类型
        if (StringUtil.isNotEmpty(param.getBusinessType())) {
            bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where code = '%s'", param.getBusinessType())));
        }
        // 价格类型
        if (StringUtil.isNotEmpty(param.getPriceType())) {
            bill.setPriceTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_price_type where name = '%s'", param.getPriceType())));
        }
        // 币种
        if (StringUtil.isNotEmpty(param.getCurrencyType())) {
            bill.setCurrencyTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_currency_type where status = 100 and name = '%s'", param.getPriceType())));
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
        if (null == bill.getBillDate()) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (null == bill.getBusinessTypeId()) {
            messageList.add(getNotNullMessage("businessTypeId"));
        }
        if (StringUtil.isEmpty(bill.getManualId())) {
            messageList.add(getNotNullMessage("manualId"));
        }
        if (null == bill.getChannelId()) {
            messageList.add(getNotNullMessage("channelCode"));
        }
        if (null == bill.getToChannelId()) {
            messageList.add(getNotNullMessage("toChannelCode"));
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
            Integer count = noticeBillDao.selectCount(new QueryWrapper<NoticeBill>().eq("manual_id", bill.getManualId()));
            if (null != count && count > 0) {
                messageList.add(getMessageByParams("dataExist", new String[]{LanguageUtil.getMessage("manualId")}));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }
        /****************   货品明细    ******************/
        List<NoticeBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        List<NoticeBillSize> billSizeList = new ArrayList<>();
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
        List<NoticeBillSize> sizeList = new ArrayList<>();
        int i = 0;
        for (NoticeBillBillGoodsDetailData item : param.getGoodsDetailData()) {
            i++;
            NoticeBillSize size = new NoticeBillSize();
            sizeList.add(size);

            size.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
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
                size.setLongId(longMap.get(item.getLongName()));
                size.setColorId(colorMap.get(item.getColorCode()));
                size.setSizeId(sizeMap.get(size.getGoodsId() + StrUtil.UNDERLINE + item.getSize()));
            }
            if (null == size.getGoodsId()) {
                messageList.add(getNotExistMessage(i, "goodsCode"));
            }
            if (null == size.getColorId()) {
                messageList.add(getNotExistMessage(i, "colorCode"));
            }
            if (null == size.getLongId()) {
                messageList.add(getNotExistMessage(i, "longName"));
            }
            if (null == size.getSizeId()) {
                messageList.add(getNotExistMessage(i, "size"));
            }
            if (CollUtil.isEmpty(messageList)) {
                // 设置货品ID
                item.setGoodsId(size.getGoodsId());
                item.setColumnId(size.getId());

            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }
        // TODO 不存在结算价的货品
        List<Long> goodsIdList = param.getGoodsDetailData().stream().filter(f -> null == f.getBalancePrice()).map(v -> v.getGoodsId()).distinct().collect(Collectors.toList());
        // TODO 获取分销价格
        for (NoticeBillBillGoodsDetailData item : param.getGoodsDetailData()) {
            // TODO 结算价不存在,则通过分销价格获取
            if (null == item.getBalancePrice() && goodsIdList.contains(item.getGoodsId())) {

            }
            if (null == item.getBalancePrice()) {
                messageList.add(getNotExistMessage(i, "balancePrice"));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }
        // TODO 存在计划单，根据货品+结算价匹配,获取计划单货品ID和欠数，不存在或超出欠数报错，更新计划单欠数
        if (null != bill.getSalePlanId()) {
        } else {
            // 尺码根据行ID分组
            Map<Long, NoticeBillSize> noticeBillSizeMap = sizeList.stream().collect(Collectors.toMap(v -> v.getId(), Function.identity()));
            // 根据货品+价格分组，支持同款多价
            param.getGoodsDetailData().stream().collect(Collectors.groupingBy(v -> v.getSameGoodsDiffPriceKey())).forEach((key, sizes) -> {
                NoticeBillGoods billGoods = NoticeBillGoods.build();
                billGoodsList.add(billGoods);

                billGoods.setBillId(bill.getId());
                NoticeBillBillGoodsDetailData detailData = sizes.get(0);
                billGoods.setGoodsId(detailData.getGoodsId());
                billGoods.setTagPrice(detailData.getTagPrice());
                billGoods.setBalancePrice(detailData.getBalancePrice());
                billGoods.setDiscount(detailData.getDiscount());
                billGoods.setCurrencyPrice(detailData.getCurrencyPrice());
                billGoods.setExchangeRate(detailData.getExchangeRate());
                billGoods.setQuantity(detailData.getQuantity());
                billGoods.setRemark(detailData.getRemark());
                // 尺码明细
                sizes.forEach(item -> {
                    NoticeBillSize billSize = noticeBillSizeMap.get(item.getColumnId());
                    billSizeList.add(billSize);

                    billSize.setBillGoodsId(billGoods.getId());
                });

            });
        }
        return null;
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
