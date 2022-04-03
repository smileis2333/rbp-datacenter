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
import com.regent.rbp.api.core.retail.RetailDistributionBill;
import com.regent.rbp.api.core.retail.RetailDistributionBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailDistributionBillGoods;
import com.regent.rbp.api.core.retail.RetailDistributionBillLogisticsInfo;
import com.regent.rbp.api.core.retail.RetailDistributionBillRelation;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillCustomerInfoDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillCustomerInfoDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailDistributionBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailDistributionBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailDistributionBillSaveParam;
import com.regent.rbp.api.service.retail.RetailDistributionBillGoodsService;
import com.regent.rbp.api.service.retail.RetailDistributionBillLogisticsInfoService;
import com.regent.rbp.api.service.retail.RetailDistributionBillRelationService;
import com.regent.rbp.api.service.retail.RetailDistributionBillService;
import com.regent.rbp.api.service.retail.RetailOrderBillGoodsService;
import com.regent.rbp.api.service.retail.context.RetailDistributionBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.bill.entity.LogisticsCompany;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2022-03-31
 * @Description
 */
@Service
public class RetailDistributionBillServiceBean extends ServiceImpl<RetailDistributionBillDao, RetailDistributionBill> implements RetailDistributionBillService {

    @Autowired
    private RetailDistributionBillDao retailDistributionBillDao;

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Autowired
    private RetailOrderBillGoodsDao retailOrderBillGoodsDao;

    @Autowired
    private SystemCommonService systemCommonService;

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
    private RetailOrderBillCustomerInfoDao retailOrderBillCustomerInfoDao;

    @Autowired
    private RetailDistributionBillGoodsService retailDistributionBillGoodsService;

    @Autowired
    private RetailOrderBillGoodsService retailOrderBillGoodsService;

    @Autowired
    private RetailDistributionBillRelationService retailDistributionBillRelationService;

    @Autowired
    private RetailDistributionBillLogisticsInfoService retailDistributionBillLogisticsInfoService;

    @Autowired
    private RetailDistributionBillCustomerInfoDao retailDistributionBillCustomerInfoDao;

    @Autowired
    private DbService dbService;

    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailDistributionBillSaveParam param) {
        RetailDistributionBillSaveContext context = new RetailDistributionBillSaveContext();
        // 参数验证
        String msg = this.convertSaveContext(context, param);
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, msg);
        }
        // 保存配单
        String billNo = this.saveRetailDisrtibutionbill(context);

        return ModelDataResponse.Success(billNo);
    }

    /**
     * 保存
     * @param context
     * @return
     */
    private String saveRetailDisrtibutionbill(RetailDistributionBillSaveContext context) {

        if (CollUtil.isNotEmpty(context.getBillGoodsList())) {
            retailDistributionBillGoodsService.saveBatch(context.getBillGoodsList());
        }
        if (CollUtil.isNotEmpty(context.getBillLogisticsInfoList())) {
            retailDistributionBillLogisticsInfoService.saveBatch(context.getBillLogisticsInfoList());
        }
        if (CollUtil.isNotEmpty(context.getBillRelationList())) {
            retailDistributionBillRelationService.saveBatch(context.getBillRelationList());
        }
        if (context.getBillCustomerInfo() != null) {
            retailDistributionBillCustomerInfoDao.insert(context.getBillCustomerInfo());
        }
        if (context.getBill() != null) {
            retailDistributionBillDao.insert(context.getBill());
        }
        // 批量更新全渠道订单货品状态
        if (CollUtil.isNotEmpty(context.getUpdateOrderBillGoodsList())) {
            retailOrderBillGoodsService.updateBatchById(context.getUpdateOrderBillGoodsList());
        }
        // 更新全渠道订单状态,原状态为已审核则转为部分配货/已配货，配货状态改为已配货
        for (Long retailOrderBillId : context.getBillGoodsList().stream().map(RetailDistributionBillGoods::getRetailOrderBillId).distinct().collect(Collectors.toList())) {
            retailOrderBillDao.updateDistributionStatus(retailOrderBillId, ThreadLocalGroup.getUserId());
        }

        return context.getBill().getBillNo();
    }

    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private String convertSaveContext(RetailDistributionBillSaveContext context, RetailDistributionBillSaveParam param) {
        List<String> messageList = new ArrayList<>();
        RetailDistributionBill bill = new RetailDistributionBill();
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            messageList.add(getNotNullMessage("goodsDetailData"));
            return String.join(StrUtil.COMMA, messageList);
        }
        if (ObjectUtil.isEmpty(param.getBillDate())) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (StrUtil.isEmpty(param.getManualId())) {
            messageList.add(getNotNullMessage("manualNo"));
        } else {
            Integer count = retailDistributionBillDao.selectCount(new LambdaQueryWrapper<RetailDistributionBill>().eq(RetailDistributionBill::getManualId, param.getManualId()));
            if (count != null && count > 0) {
                messageList.add(getMessageByParams("dataRepeated", new String[]{LanguageUtil.getMessage("manualNo")}));
            }
        }

        Long id = SnowFlakeUtil.getDefaultSnowFlakeId();
        bill.setId(id);
        bill.preInsert();
        // TODO 模块id
//        ModuleBusinessType moduleBusinessType = baseDbDao.getOneModuleBusinessType(InformationConstants.ModuleConstants.RETAIL_DISTRIBUTION_BILL, InformationConstants.SystemConstants.DEFAULT_BUSINESS_TYPE_ID);
//        bill.setModuleId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getModuleId));
        String subModuleId = "701072";
        bill.setModuleId(subModuleId);
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        // 渠道编码
        if (StrUtil.isNotEmpty(param.getChannelCode())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
            if (ObjectUtil.isEmpty(bill.getChannelId())) {
                messageList.add(getNotExistMessage("channelCode"));
            }
        } else {
            messageList.add(getNotNullMessage("channelCode"));
        }
        bill.setStatus(param.getStatus());
        bill.setAcceptOrderStatus(param.getAcceptOrderStatus());
        bill.setPrintStatus(param.getPrintStatus());
        bill.setPrintCount(param.getPrintCount());

        // 配单明细
        List<RetailDistributionBillGoods> distributionBillGoodsList = new ArrayList<>();
        // 订单明细更新
        List<RetailOrderBillGoods> updateOrderBillGoodsList = new ArrayList<>();
        // TODO 线上单号
        List<RetailOrderBill> retailOrderBillList = retailOrderBillDao.selectList(new LambdaQueryWrapper<RetailOrderBill>()
                .in(RetailOrderBill::getBillNo, StreamUtil.toSet(param.getGoodsDetailData(), RetailDistributionBillGoodsDetailData::getRetailOrderBillNo)));
        List<Integer> statusList = Arrays.asList(0,2,3);
        for (RetailOrderBill retailOrderBill : retailOrderBillList) {
            if (statusList.contains(retailOrderBill.getStatus())) {
                messageList.add("订单:" + retailOrderBill.getBillNo() + " 状态未审核、反审核或已作废");
            }
        }
        List<Long> toChannelIdList = retailOrderBillList.stream().map(RetailOrderBill::getChannelId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (toChannelIdList.size() != 1) {
            messageList.add(getMessageByParams("retailOrderChannelDiff", null));
            return String.join(StrUtil.COMMA, messageList);
        }
        bill.setToChannelId(toChannelIdList.get(0));
        Map<String, RetailOrderBill> retailOrderBillMap = retailOrderBillList.stream().collect(Collectors.toMap(RetailOrderBill::getBillNo, Function.identity(), (v1, v2) -> v1));
        List<RetailOrderBillGoods> retailOrderBillGoodsList = retailOrderBillGoodsDao.selectList(new LambdaQueryWrapper<RetailOrderBillGoods>()
                .in(RetailOrderBillGoods::getBillId, StreamUtil.toSet(retailOrderBillList, RetailOrderBill::getId))
                .eq(RetailOrderBillGoods::getProcessStatus, 0)
                .orderByAsc(RetailOrderBillGoods::getBillId));
        Map<Long, List<RetailOrderBillGoods>> retailOrderBillGoodsMap = retailOrderBillGoodsList.stream().collect(Collectors.groupingBy(RetailOrderBillGoods::getBillId));

        // TODO 判空
        Map<String, Long> goodsMap = goodsDao.selectList(new QueryWrapper<Goods>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), RetailDistributionBillGoodsDetailData::getGoodsCode))).stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorDao.selectList(new QueryWrapper<Color>().in("code", StreamUtil.toSet(param.getGoodsDetailData(), RetailDistributionBillGoodsDetailData::getColorCode))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longDao.selectList(new QueryWrapper<LongInfo>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), RetailDistributionBillGoodsDetailData::getLongName))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<String, Long> sizeMap = sizeDetailDao.selectList(new QueryWrapper<SizeDetail>().in("name", StreamUtil.toSet(param.getGoodsDetailData(), RetailDistributionBillGoodsDetailData::getSize))).stream().collect(Collectors.toMap(SizeDetail::getName, SizeDetail::getId));
        for (RetailDistributionBillGoodsDetailData goods : param.getGoodsDetailData()) {
            // TODO 校验计量货品
            if (BigDecimal.ONE.compareTo(goods.getQuantity()) != 0) {
                messageList.add(getMessageByParams("goodsQuantityMustBeOne", null));
                continue;
            }
            RetailOrderBill retailOrderBill = retailOrderBillMap.get(goods.getRetailOrderBillNo());
            if (null == retailOrderBill) {
                messageList.add(getNotExistMessage("retailOrderBillNo"));
                continue;
            }
            List<RetailOrderBillGoods> orderBillGoodsList = retailOrderBillGoodsMap.get(retailOrderBill.getId());
            if (CollUtil.isEmpty(orderBillGoodsList)) {
                messageList.add(getMessageByParams("sendSkuDataError", new String[]{LanguageUtil.getMessage(
                        StrUtil.join(StrUtil.DASHED, goods.getRetailOrderBillNo(), goods.getBarcode(), goods.getGoodsCode(),
                                goods.getColorCode(), goods.getLongName(), goods.getSize()))}));
                continue;
            }
            Long goodsId = goodsMap.get(goods.getGoodsCode());
            Long colorId = colorMap.get(goods.getColorCode());
            Long longId = longMap.get(goods.getLongName());
            Long sizeId = sizeMap.get(goods.getSize());

            boolean flag = true;
            Iterator<RetailOrderBillGoods> iterator = orderBillGoodsList.iterator();
            while (iterator.hasNext()) {
                RetailOrderBillGoods detail = iterator.next();
                if (ObjectUtil.equal(detail.getBarcode(), goods.getBarcode()) ||
                        (ObjectUtil.equal(detail.getGoodsId(), goodsId)
                                && ObjectUtil.equal(detail.getColorId(), colorId)
                                && ObjectUtil.equal(detail.getLongId(), longId)
                                && ObjectUtil.equal(detail.getSizeId(), sizeId))) {

                    //订单货品处理状态：原状态为未处理的货品转为已配货，否则不变
                    if (detail.getProcessStatus().equals(0)) {
                        RetailOrderBillGoods orderBillGoods = new RetailOrderBillGoods();
                        orderBillGoods.preUpdate();
                        orderBillGoods.setId(detail.getId());
                        orderBillGoods.setProcessStatus(1);
                        updateOrderBillGoodsList.add(orderBillGoods);
                    }
                    // 配单货品
                    RetailDistributionBillGoods entity = new RetailDistributionBillGoods();
                    entity.preInsert();
                    entity.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    entity.setBillId(bill.getId());
                    entity.setRetailOrderBillGoodsId(detail.getId());
                    entity.setRetailOrderBillId(detail.getBillId());
                    entity.setGoodsId(detail.getGoodsId());
                    entity.setColorId(detail.getColorId());
                    entity.setLongId(detail.getLongId());
                    entity.setSizeId(detail.getSizeId());
                    entity.setBarcode(detail.getBarcode());
                    entity.setTagPrice(detail.getTagPrice());
                    entity.setBalancePrice(detail.getBalancePrice());
                    entity.setQuantity(goods.getQuantity());
                    entity.setDiscount(detail.getDiscount());
                    distributionBillGoodsList.add(entity);

                    flag = false;
                    iterator.remove();
                    break;
                }
            }
            if (flag) {
                messageList.add(getMessageByParams("sendSkuDataError", new String[]{LanguageUtil.getMessage(
                        StrUtil.join(StrUtil.DASHED, goods.getRetailOrderBillNo(), goods.getBarcode(), goods.getGoodsCode(),
                                goods.getColorCode(), goods.getLongName(), goods.getSize()))}));
            }
        }

        if (CollUtil.isNotEmpty(messageList)) {
            return String.join(StrUtil.COMMA, messageList);
        }

        // 顾客信息
        RetailDistributionBillCustomerInfo customerInfo = new RetailDistributionBillCustomerInfo();
        List<RetailDistributionBillRelation> billRelationList = new ArrayList<>();
        int i = 1;
        for (Long retailOrderBillId : distributionBillGoodsList.stream().map(RetailDistributionBillGoods::getRetailOrderBillId).distinct().collect(Collectors.toList())) {
            if (i == 1) {
                // 拷贝全渠道顾客信息，取第一张订单的
                BeanUtils.copyProperties(retailOrderBillCustomerInfoDao.selectOne(new LambdaQueryWrapper<RetailOrderBillCustomerInfo>()
                        .eq(RetailOrderBillCustomerInfo::getBillId, retailOrderBillId)), customerInfo);
                customerInfo.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                customerInfo.setBillId(bill.getId());
            }
            // 关联配单
            RetailDistributionBillRelation relation = new RetailDistributionBillRelation();
            relation.preInsert();
            relation.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            relation.setBillId(bill.getId());
            relation.setRetailOrderBillId(retailOrderBillId);
            billRelationList.add(relation);
            i++;
        }

        // 物流信息
        if (CollUtil.isEmpty(param.getLogisticsInfo())) {
            messageList.add(getNotNullMessage("logisticsInfo"));
            return String.join(StrUtil.COMMA, messageList);
        }
        List<RetailDistributionBillLogisticsInfo> billLogisticsInfoList = new ArrayList<>();
        for (RetailDistributionBillLogisticsInfoDto logisticsInfoDto : param.getLogisticsInfo()) {
            RetailDistributionBillLogisticsInfo logisticsInfo = new RetailDistributionBillLogisticsInfo();
            logisticsInfo.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            logisticsInfo.setBillId(bill.getId());
            if (StrUtil.isEmpty(logisticsInfoDto.getLogisticsBillCode())) {
                messageList.add(getNotNullMessage("logisticsBillCode"));
                continue;
            }
            logisticsInfo.setLogisticsBillCode(logisticsInfoDto.getLogisticsBillCode());
            if (StrUtil.isEmpty(logisticsInfoDto.getLogisticsCompanyCode())) {
                messageList.add(getNotNullMessage("logisticsCompanyCode"));
                continue;
            }
            // 物流公司信息
            List<IdNameCodeDto> logisticsCompanyList = dbService.selectIdNameCodeList(new QueryWrapper<LogisticsCompany>().eq("code", logisticsInfoDto.getLogisticsCompanyCode()), LogisticsCompany.class);
            if (CollUtil.isNotEmpty(logisticsCompanyList)) {
                logisticsInfo.setLogisticsCompanyId(logisticsCompanyList.get(0).getId());
            } else {
                messageList.add(getNotExistMessage("logisticsCompanyCode"));
                continue;
            }
            logisticsInfo.setLogisticsAmount(logisticsInfoDto.getLogisticsAmount());
            logisticsInfo.preInsert();
            billLogisticsInfoList.add(logisticsInfo);
        }

        context.setBill(bill);
        context.setBillGoodsList(distributionBillGoodsList);
        context.setBillRelationList(billRelationList);
        context.setBillCustomerInfo(customerInfo);
        context.setUpdateOrderBillGoodsList(updateOrderBillGoodsList);
        context.setBillLogisticsInfoList(billLogisticsInfoList);

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
