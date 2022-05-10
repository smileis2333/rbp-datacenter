package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.CurrencyType;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.NumberUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.yumei.dao.FinancialSettlementBillDao;
import com.regent.rbp.task.yumei.dto.FinancialSettlementBillContext;
import com.regent.rbp.task.yumei.model.YumeiFinancialSettlementBill;
import com.regent.rbp.task.yumei.model.YumeiFinancialSettlementBillGoods;
import com.regent.rbp.task.yumei.param.YumeiFinancialSettlementBillGoodsParam;
import com.regent.rbp.task.yumei.param.YumeiFinancialSettlementBillSaveParam;
import com.regent.rbp.task.yumei.service.FinancialSettlementBillGoodsService;
import com.regent.rbp.task.yumei.service.FinancialSettlementBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@Slf4j
@Service
public class FinancialSettlementBillServiceImpl extends ServiceImpl<FinancialSettlementBillDao, YumeiFinancialSettlementBill> implements FinancialSettlementBillService {

    @Autowired
    private FinancialSettlementBillDao financialSettlementBillDao;

    @Autowired
    private FinancialSettlementBillGoodsService financialSettlementBillGoodsService;

    @Autowired
    private SystemCommonService systemCommonService;

    @Autowired
    private FundAccountDao fundAccountDao;

    @Autowired
    private CurrencyTypeDao currencyTypeDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private LongDao longDao;

    @Autowired
    private ColorDao colorDao;

    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Autowired
    private BarcodeDao barcodeDao;

    @Override
    public ModelDataResponse<String> save(YumeiFinancialSettlementBillSaveParam param) {
        if (null == param) {
            return ModelDataResponse.errorParameter("参数不能为空");
        }
        log.info("结算单 请求参数:" + param.toString());

        FinancialSettlementBillContext context = new FinancialSettlementBillContext();
        // 参数验证
        String msg = this.convertSaveContext(context, param);
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, msg);
        }
        // 保存
        String billNo = this.saveYumeiFinancialSettlementBill(context);
        log.info("结算单 生成单号:" + billNo);
        return ModelDataResponse.Success(billNo);
    }

    private String saveYumeiFinancialSettlementBill(FinancialSettlementBillContext context) {
        if (CollUtil.isNotEmpty(context.getBillGoodsList())) {
            financialSettlementBillGoodsService.saveBatch(context.getBillGoodsList());
        }
        financialSettlementBillDao.insert(context.getBill());

        return context.getBill().getBillNo();
    }

    private String convertSaveContext(FinancialSettlementBillContext context, YumeiFinancialSettlementBillSaveParam param) {
        List<String> messageList = new ArrayList<>();
        YumeiFinancialSettlementBill bill = new YumeiFinancialSettlementBill();
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            messageList.add(getNotNullMessage("goodsDetailData"));
            return String.join(StrUtil.COMMA, messageList);
        }
        if (ObjectUtil.isEmpty(param.getBillDate())) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (ObjectUtil.isEmpty(param.getStatus())) {
            messageList.add(getNotNullMessage("status"));
        }
        if (StrUtil.isEmpty(param.getManualId())) {
            messageList.add(getNotNullMessage("manualId"));
        } else {
            Integer count = financialSettlementBillDao.selectCount(new LambdaQueryWrapper<YumeiFinancialSettlementBill>().eq(YumeiFinancialSettlementBill::getManualId, param.getManualId()));
            if (count != null && count > 0) {
                messageList.add(getMessageByParams("dataRepeated", new String[]{LanguageUtil.getMessage("manualNo")}));
            }
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }

        Long id = SnowFlakeUtil.getDefaultSnowFlakeId();
        bill.setId(id);
        bill.preInsert();
        String subModuleId = "805001";
        bill.setModuleId(subModuleId);
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setStatus(param.getStatus());
        bill.setNotes(param.getNotes());
        bill.setOnlineOrderCode(param.getOnlineOrderCode());
        // 资金号
        if (StrUtil.isEmpty(param.getFundAccount())) {
            messageList.add(getNotNullMessage("fundAccount"));
        }
        FundAccount fundAccount = fundAccountDao.selectOne(new LambdaQueryWrapper<FundAccount>().eq(FundAccount::getCode, param.getFundAccount()));
        if (null == fundAccount) {
            messageList.add(getNotExistMessage("fundAccount"));
        } else {
            bill.setFundAccountId(fundAccount.getId());
        }
        // 币种
        if (StrUtil.isNotEmpty(param.getCurrencyType())) {
            CurrencyType currencyType = currencyTypeDao.selectOne(new LambdaQueryWrapper<CurrencyType>().eq(CurrencyType::getName, param.getCurrencyType()));
            if (null != currencyType) {
                bill.setCurrencyTypeId(currencyType.getId());
            }
        }

        // 货品明细
        List<YumeiFinancialSettlementBillGoods> billGoodsList = new ArrayList<>();
        BigDecimal sumSkuQuantity = BigDecimal.ZERO;
        BigDecimal sumTagAmount = BigDecimal.ZERO;
        BigDecimal sumAmount = BigDecimal.ZERO;
        Map<String, Long> goodsMap = goodsDao.selectList(new QueryWrapper<Goods>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), YumeiFinancialSettlementBillGoodsParam::getGoodsCode))).stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), YumeiFinancialSettlementBillGoodsParam::getColorCode))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), YumeiFinancialSettlementBillGoodsParam::getLongName))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<String, Long> sizeMap = sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), YumeiFinancialSettlementBillGoodsParam::getSize))).stream().collect(Collectors.toMap(SizeDetail::getName, SizeDetail::getId));
        Map<String, Barcode> barcodeMap = barcodeDao.selectList(new LambdaQueryWrapper<Barcode>().in(Barcode::getBarcode, StreamUtil.toSet(param.getGoodsDetailData(), YumeiFinancialSettlementBillGoodsParam::getBarcode))).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        for (YumeiFinancialSettlementBillGoodsParam goods : param.getGoodsDetailData()) {
            if (BigDecimal.ZERO.compareTo(goods.getQuantity()) >= 0) {
                messageList.add(StrUtil.join(StrUtil.DASHED, goods.getBarcode(), goods.getGoodsCode(),
                        goods.getColorCode(), goods.getLongName(), goods.getSize()) + "货品数量不能小于等于0");
                continue;
            }
            Barcode barcode = barcodeMap.get(goods.getBarcode());
            Long goodsId = OptionalUtil.ofNullable(barcode, Barcode::getGoodsId, goodsMap.get(goods.getGoodsCode()));
            Long colorId = OptionalUtil.ofNullable(barcode, Barcode::getColorId, colorMap.get(goods.getColorCode()));
            Long longId = OptionalUtil.ofNullable(barcode, Barcode::getLongId, longMap.get(goods.getLongName()));
            Long sizeId = OptionalUtil.ofNullable(barcode, Barcode::getSizeId, sizeMap.get(goods.getSize()));

            YumeiFinancialSettlementBillGoods entity = new YumeiFinancialSettlementBillGoods();
            entity.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            entity.setBillId(bill.getId());
            entity.setGoodsId(goodsId);
            entity.setColorId(colorId);
            entity.setLongId(longId);
            entity.setSizeId(sizeId);
            entity.setTagPrice(goods.getTagPrice() == null ? BigDecimal.ZERO : goods.getTagPrice());
            entity.setBalancePrice(goods.getBalancePrice() == null ? BigDecimal.ZERO : goods.getBalancePrice());
            entity.setQuantity(goods.getQuantity());
            entity.setDiscount(goods.getDiscount() == null ? BigDecimal.ZERO : goods.getDiscount());
            entity.setRemark(goods.getRemark());
            entity.setCurrencyPrice(goods.getCurrencyPrice());
            entity.setExchangeRate(goods.getExchangeRate());
            entity.setAmount(goods.getAmount() == null ? BigDecimal.ZERO : goods.getAmount());

            billGoodsList.add(entity);
            sumSkuQuantity = NumberUtil.add(sumSkuQuantity, entity.getQuantity());
            sumTagAmount = NumberUtil.add(sumTagAmount, NumberUtil.mul(entity.getTagPrice(), entity.getQuantity()));
            sumAmount = NumberUtil.add(sumAmount, entity.getAmount());
        }
        bill.setSumSkuQuantity(sumSkuQuantity);
        bill.setSumTagAmount(sumTagAmount);
        bill.setSumAmount(sumAmount);
        context.setBill(bill);
        context.setBillGoodsList(billGoodsList);
        return String.join(StrUtil.COMMA, messageList);
    }

    private static String getNotExistMessage(String key) {
        return getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotNullMessage(String key) {
        return getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage(key)});
    }

    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }
}
