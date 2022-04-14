package com.regent.rbp.api.service.bean.coupon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.coupon.*;
import com.regent.rbp.api.dao.base.RetailPayTypeDao;
import com.regent.rbp.api.dao.coupon.*;
import com.regent.rbp.api.service.coupon.CouponModelService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2022/4/14 11:13
 */
@Service
public class CouponModelServiceBean implements CouponModelService {
    @Autowired
    private CouponModelDao couponModelDao;
    @Autowired
    private CouponRuleChannelRangeDao couponRuleChannelRangeDao;
    @Autowired
    private CouponRuleChannelRangeValueDao couponRuleChannelRangeValueDao;
    @Autowired
    private CouponRuleGoodsRangeDao couponRuleGoodsRangeDao;
    @Autowired
    private CouponRuleGoodsRangeValueDao couponRuleGoodsRangeValueDao;
    /**
     * 折扣券
     */
    private static final String DISCOUNT_COUPON = "4";
    /**
     * 抵用券
     */
    private static final String CASH_COUPON = "1";

    @Autowired
    private RetailPayTypeDao retailPayTypeDao;
    @Autowired
    private CouponModelCashPropertyDao couponModelCashPropertyDao;
    @Autowired
    private CouponModelDiscountPropertyDao couponModelDiscountPropertyDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCouponModel(List<CouponModel> couponModelList) {
        if (CollectionUtils.isEmpty(couponModelList)) {
            return;
        }
        for (CouponModel data : couponModelList) {
            if (data.getBonusType().equals(DISCOUNT_COUPON)) {
                data.setType(2);
            } else {
                data.setType(1);
            }
            CouponModel oldCouponModel = couponModelDao.selectOne(new QueryWrapper<CouponModel>().eq("model_no", data.getModelNo()));
            Long couponModelId;
            if (oldCouponModel == null) {
                data.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                data.setCreatedTime(new Date());
                data.setCheckTime(new Date());
                data.setUpdatedTime(new Date());
                couponModelDao.insert(data);
                couponModelId = data.getId();
            } else {
                couponModelId = oldCouponModel.getId();
                oldCouponModel.setUpdatedTime(new Date());
                couponModelDao.update(oldCouponModel, new QueryWrapper<CouponModel>().eq("id", oldCouponModel.getId()));
            }
            if (data.getBonusType().equals(DISCOUNT_COUPON)) {
                couponModelDiscountPropertyDao.delete(new QueryWrapper<CouponModelDiscountProperty>().eq("coupon_model_id", couponModelId));
                CouponModelDiscountProperty property = new CouponModelDiscountProperty();
                property.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                property.setCouponModelId(couponModelId);
                if (StringUtils.isNotEmpty(data.getDiscount())) {
                    property.setDiscount(new BigDecimal(Double.parseDouble(data.getDiscount()) * 100));
                }
                if (StringUtils.isNotEmpty(data.getDiscount())) {
                    property.setMaxSalesAmount(new BigDecimal(data.getMaxAmount()));
                }
                property.setRestoreTagPrice(0);
                property.setLowerLimitDiscount(new BigDecimal("1.0"));
                couponModelDiscountPropertyDao.insert(property);
            } else if(data.getBonusType().equals(CASH_COUPON)){
                couponModelCashPropertyDao.delete(new QueryWrapper<CouponModelCashProperty>().eq("coupon_model_id", couponModelId));
                CouponModelCashProperty property = new CouponModelCashProperty();
                property.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                property.setCouponModelId(couponModelId);
                if (StringUtils.isNotEmpty(data.getTypeMoney())) {
                    property.setAmount(new BigDecimal(data.getTypeMoney()));
                }
                if (StringUtils.isNotEmpty(data.getMinGoodsAmount())) {
                    property.setBalancePriceLimit(new BigDecimal(data.getMinGoodsAmount()));
                }
                if (StringUtils.isNotEmpty(data.getPaymentCode())) {
                    RetailPayType oldRetailPayType = retailPayTypeDao.selectOne(new QueryWrapper<RetailPayType>().eq("code", data.getPaymentCode()));
                    if (oldRetailPayType == null) {
                        RetailPayType retailPayType = new RetailPayType();
                        retailPayType.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                        retailPayType.setCode(data.getPaymentCode());
                        retailPayType.setName("inno券支付方式");
                        retailPayType.setRetailPayPlatform("6");
                        retailPayType.setRetailPayPlatformId(1609200376474119L);
                        retailPayType.setUseAble(1);
                        retailPayType.setVisible(1);
                        retailPayType.setNotIncome(0);
                        retailPayType.setNotInPoint(0);
                        retailPayType.setPos(1);
                        retailPayType.setSystemInline(0);
                        retailPayType.setDpPrice(0);
                        retailPayType.setLimitRatio(0);
                        retailPayType.setCreatedTime(new Date());
                        retailPayType.setUpdatedTime(new Date());
                        retailPayTypeDao.insert(retailPayType);
                        property.setPayId(retailPayType.getId());
                    } else {
                        property.setPayId(oldRetailPayType.getId());
                    }
                }
                property.setOverlayNumLimit(0);
                property.setSelfOverlayNumLimit(0);
                couponModelCashPropertyDao.insert(property);
            }

            couponRuleChannelRangeDao.delete(new QueryWrapper<CouponRuleChannelRange>().eq("coupon_rule_id", couponModelId));
            couponRuleChannelRangeValueDao.delete(new QueryWrapper<CouponRuleChannelRangeValue>().eq("coupon_rule_id", couponModelId));
            couponRuleGoodsRangeDao.delete(new QueryWrapper<CouponRuleGoodsRange>().eq("coupon_rule_id", couponModelId));
            couponRuleGoodsRangeValueDao.delete(new QueryWrapper<CouponRuleGoodsRangeValue>().eq("coupon_rule_id", couponModelId));
            //店铺范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleChannelRangeList())) {
                for (CouponRuleChannelRange couponRuleChannelRange : data.getCouponRuleChannelRangeList()) {
                    couponRuleChannelRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleChannelRange.setCouponRuleId(couponModelId);
                    couponRuleChannelRange.setCreatedTime(new Date());
                    couponRuleChannelRangeDao.insert(couponRuleChannelRange);
                    if (CollectionUtils.isNotEmpty(couponRuleChannelRange.getCouponRuleChannelRangeValueList())) {
                        for (CouponRuleChannelRangeValue value : couponRuleChannelRange.getCouponRuleChannelRangeValueList()) {
                            value.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                            value.setCouponRuleId(couponModelId);
                            value.setCouponRuleChannelRangeId(couponRuleChannelRange.getId());
                            value.setCreatedTime(new Date());
                            couponRuleChannelRangeValueDao.insert(value);
                        }
                    }
                }
            }
            //货品范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleGoodsRangeList())) {
                for (CouponRuleGoodsRange couponRuleGoodsRange : data.getCouponRuleGoodsRangeList()) {
                    couponRuleGoodsRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleGoodsRange.setCouponRuleId(couponModelId);
                    couponRuleGoodsRange.setCreatedTime(new Date());
                    couponRuleGoodsRangeDao.insert(couponRuleGoodsRange);
                    if (CollectionUtils.isNotEmpty(couponRuleGoodsRange.getCouponRuleGoodsRangeValueList())) {
                        for (CouponRuleGoodsRangeValue value : couponRuleGoodsRange.getCouponRuleGoodsRangeValueList()) {
                            value.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                            value.setCouponRuleId(couponModelId);
                            value.setCouponRuleGoodsRangeId(couponRuleGoodsRange.getId());
                            value.setCreatedTime(new Date());
                            couponRuleGoodsRangeValueDao.insert(value);
                        }
                    }
                }
            }
        }
    }
}
