package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.core.retail.RetailReceiveBackBillGoods;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.retail.RetailReceiveBackBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReceiveBackBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReceiveBackBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.retail.RetailReceiveBackBillGoodsService;
import com.regent.rbp.api.service.retail.RetailReceiveBackBillService;
import com.regent.rbp.api.service.retail.context.RetailReceiveBackBillSaveContext;
import com.regent.rbp.common.constants.ModuleTableConstants;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2022-04-03
 * @Description
 */
@Slf4j
@Service
public class RetailReceiveBackBillServiceBean extends ServiceImpl<RetailReceiveBackBillDao, RetailReceiveBackBill> implements RetailReceiveBackBillService {

    @Autowired
    private RetailReceiveBackBillDao retailReceiveBackBillDao;

    @Autowired
    private SystemCommonService systemCommonService;

    @Autowired
    private RetailReturnNoticeBillDao retailReturnNoticeBillDao;

    @Autowired
    private RetailReturnNoticeBillGoodsDao retailReturnNoticeBillGoodsDao;

    @Autowired
    private BaseDbDao baseDbDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private LongDao longDao;

    @Autowired
    private ColorDao colorDao;

    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Autowired
    private RetailReceiveBackBillGoodsService retailReceiveBackBillGoodsService;

    @Autowired
    private DbService dbService;

    @Autowired
    private BaseDbService baseDbService;

    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailReceiveBackBillSaveParam param) {
        RetailReceiveBackBillSaveContext context = new RetailReceiveBackBillSaveContext();
        // 参数验证
        String msg = this.convertSaveContext(context, param);
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, msg);
        }
        // 保存配单
        String billNo = this.saveRetailReceiveBackBill(context);
        return ModelDataResponse.Success(billNo);
    }

    private String saveRetailReceiveBackBill(RetailReceiveBackBillSaveContext context) {
        if (CollUtil.isNotEmpty(context.getBillGoodsList())) {
            retailReceiveBackBillGoodsService.saveBatch(context.getBillGoodsList());
        }
        // 单据自定义字段
        baseDbService.saveOrUpdateCustomFieldData(context.getBill().getModuleId(), ModuleTableConstants.RETAIL_RECEIVE_BACK_BILL, context.getBill().getId(), context.getBill().getCustomFieldMap());
        retailReceiveBackBillDao.insert(context.getBill());
        return context.getBill().getBillNo();
    }

    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private String convertSaveContext(RetailReceiveBackBillSaveContext context, RetailReceiveBackBillSaveParam param) {
        List<String> messageList = new ArrayList<>();
        RetailReceiveBackBill bill = new RetailReceiveBackBill();
        if (ObjectUtil.isEmpty(param.getBillDate())) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (StrUtil.isEmpty(param.getManualId())) {
            messageList.add(getNotNullMessage("manualNo"));
        } else {
            Integer count = retailReceiveBackBillDao.selectCount(new LambdaQueryWrapper<RetailReceiveBackBill>().eq(RetailReceiveBackBill::getManualId, param.getManualId()));
            if (count != null && count > 0) {
                messageList.add(getMessageByParams("dataRepeated", new String[]{LanguageUtil.getMessage("manualNo")}));
            }
        }
        if (StrUtil.isEmpty(param.getRetailReturnNoticeBillNo())) {
            messageList.add(getNotNullMessage("retailReturnNoticeBillNo"));
        }
        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }
        Long id = SnowFlakeUtil.getDefaultSnowFlakeId();
        bill.setId(id);
        bill.preInsert();
        // TODO 模块id
        String subModuleId = "701075";
        bill.setModuleId(subModuleId);
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setStatus(param.getStatus());

        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            bill.setCustomFieldMap(customFieldMap);
        }

        // 货品明细
        List<RetailReceiveBackBillGoods> retailReceiveBackBillGoodsList = new ArrayList<>();
        // 通知单号
        RetailReturnNoticeBill retailReturnNoticeBill = retailReturnNoticeBillDao.selectOne(new LambdaQueryWrapper<RetailReturnNoticeBill>()
                .eq(RetailReturnNoticeBill::getBillNo, param.getRetailReturnNoticeBillNo()));
        if (null == retailReturnNoticeBill) {
            messageList.add(getNotExistMessage("retailReturnNoticeBillNo"));
            return String.join(StrUtil.COMMA, messageList);
        }
        bill.setLogisticsBillCode(retailReturnNoticeBill.getLogisticsBillCode());
        bill.setLogisticsCompanyId(retailReturnNoticeBill.getLogisticsCompanyId());
        bill.setSaleChannelId(retailReturnNoticeBill.getSaleChannelId());
        bill.setReceiveChannelId(retailReturnNoticeBill.getReceiveChannelId());
        bill.setRetailReturnNoticeBillId(retailReturnNoticeBill.getId());
        List<Integer> statusList = Arrays.asList(0,2,3);
        if (statusList.contains(retailReturnNoticeBill.getStatus())) {
            messageList.add("订单:" + retailReturnNoticeBill.getBillNo() + " 状态未审核、反审核或已作废");
        }
        List<RetailReturnNoticeBillGoods> retailReturnNoticeBillGoodsList = retailReturnNoticeBillGoodsDao.selectList(new LambdaQueryWrapper<RetailReturnNoticeBillGoods>()
                .eq(RetailReturnNoticeBillGoods::getBillId, retailReturnNoticeBill.getId()));
        Map<String, Long> goodsMap = goodsDao.selectList(new QueryWrapper<Goods>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), RetailReceiveBackBillGoodsDetailData::getGoodsCode))).stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), RetailReceiveBackBillGoodsDetailData::getColorCode))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), RetailReceiveBackBillGoodsDetailData::getLongName))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<String, Long> sizeMap = sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), RetailReceiveBackBillGoodsDetailData::getSize))).stream().collect(Collectors.toMap(SizeDetail::getName, SizeDetail::getId));
        for (RetailReceiveBackBillGoodsDetailData goods : param.getGoodsDetailData()) {
            // TODO 校验计量货品
            boolean flag = true;
            for (BigDecimal i = BigDecimal.ZERO; i.compareTo(goods.getQuantity())==-1;i = i.add(BigDecimal.ONE) )
            {
                Long goodsId = goodsMap.get(goods.getGoodsCode());
                Long colorId = colorMap.get(goods.getColorCode());
                Long longId = longMap.get(goods.getLongName());
                Long sizeId = sizeMap.get(goods.getSize());
                flag = true;

                Iterator<RetailReturnNoticeBillGoods> iterator = retailReturnNoticeBillGoodsList.iterator();
                while (iterator.hasNext()) {
                    RetailReturnNoticeBillGoods detail = iterator.next();
                    if (ObjectUtil.equal(detail.getBarcode(), goods.getBarcode()) ||
                            (ObjectUtil.equal(detail.getGoodsId(), goodsId)
                                    && ObjectUtil.equal(detail.getColorId(), colorId)
                                    && ObjectUtil.equal(detail.getLongId(), longId)
                                    && ObjectUtil.equal(detail.getSizeId(), sizeId))) {

                        // 配单货品
                        RetailReceiveBackBillGoods entity = new RetailReceiveBackBillGoods();
                        entity.preInsert();
                        entity.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                        entity.setBillId(bill.getId());
                        entity.setGoodsId(detail.getGoodsId());
                        entity.setColorId(detail.getColorId());
                        entity.setLongId(detail.getLongId());
                        entity.setSizeId(detail.getSizeId());
                        entity.setBarcode(detail.getBarcode());
                        entity.setTagPrice(detail.getTagPrice());
                        entity.setDiscount(detail.getDiscount());
                        entity.setBalancePrice(detail.getBalancePrice());
                        entity.setQuantity(goods.getQuantity());
                        if (null != goods.getBalancePrice()) {
                            entity.setBalancePrice(goods.getBalancePrice());
                        }
                        // 折扣
                        if (null != entity.getBalancePrice() && entity.getTagPrice().compareTo(BigDecimal.ZERO) != 0) {
                            entity.setDiscount(entity.getBalancePrice().divide(entity.getTagPrice(), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        retailReceiveBackBillGoodsList.add(entity);

                        flag = false;

                        iterator.remove();
                        break;
                    }
                }

            }
            if (flag) {
                messageList.add(getMessageByParams("sendSkuDataError", new String[]{LanguageUtil.getMessage(
                        StrUtil.join(StrUtil.DASHED, goods.getBarcode(), goods.getGoodsCode(),
                                goods.getColorCode(), goods.getLongName(), goods.getSize()))}));
            }
        }

        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }

        context.setBill(bill);
        context.setBillGoodsList(retailReceiveBackBillGoodsList);

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
