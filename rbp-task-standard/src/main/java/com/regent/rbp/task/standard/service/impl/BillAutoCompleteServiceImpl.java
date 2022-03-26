package com.regent.rbp.task.standard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.BillMasterData;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoodsFinal;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBill;
import com.regent.rbp.api.core.purchaseReceiveBill.PurchaseReceiveBillGoods;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoodsFinal;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.noticeBill.NoticeBillGoodsDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillGoodsFinalDao;
import com.regent.rbp.api.dao.purchaseReceiveBill.PurchaseReceiveBillDao;
import com.regent.rbp.api.dao.purchaseReceiveBill.PurchaseReceiveBillGoodsDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillGoodsFinalDao;
import com.regent.rbp.api.dao.sendBill.SendBillDao;
import com.regent.rbp.task.standard.module.WeekDate;
import com.regent.rbp.task.standard.module.param.BillParam;
import com.regent.rbp.task.standard.service.BillAutoCompleteService;
import com.regent.rbp.task.standard.util.DateUtil;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 单据自动完成 Impl
 * @author: HaiFeng
 * @create: 2021-11-15 14:50
 */
@Service
public class BillAutoCompleteServiceImpl implements BillAutoCompleteService {

    @Autowired
    SalePlanBillDao salePlanBillDao;
    @Autowired
    SalePlanBillGoodsFinalDao salePlanBillGoodsFinalDao;
    @Autowired
    NoticeBillDao noticeBillDao;
    @Autowired
    NoticeBillGoodsDao noticeBillGoodsDao;
    @Autowired
    SendBillDao sendBillDao;
    @Autowired
    PurchaseBillDao purchaseBillDao;
    @Autowired
    PurchaseBillGoodsFinalDao purchaseBillGoodsFinalDao;
    @Autowired
    PurchaseReceiveBillDao purchaseReceiveBillDao;
    @Autowired
    PurchaseReceiveBillGoodsDao purchaseReceiveBillGoodsDao;


    @Override
    public void salePlanBillAutoComplete(BillParam billParam) {
        Date date = salePlanBillDao.queryMinDate();
        List<WeekDate> weekDateList = DateUtil.doDateType(date.getTime(), new Date().getTime());
        for (WeekDate weekDate : weekDateList) {
            // 查询未完结的计划单
            QueryWrapper queryWrapper = new QueryWrapper();
            if (StringUtils.isNotBlank(billParam.getBillNo())) {
                queryWrapper.eq("bill_no", billParam.getBillNo());
            }
            queryWrapper.ne("finish_flag", 1);
            queryWrapper.eq("status", 1);
            queryWrapper.ge("created_time", weekDate.getStartTime());
            queryWrapper.le("created_time", weekDate.getEndTime());
            List<SalePlanBill> salePlanBillList = salePlanBillDao.selectList(queryWrapper);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
            XxlJobHelper.log(String.format("查出 %s ~ %s 未完结 计划单 %s 单", simpleDateFormat.format(weekDate.getStartTime()), simpleDateFormat.format(weekDate.getEndTime()), salePlanBillList.size()));
            Integer pedometer = 0;
            for (SalePlanBill bill : salePlanBillList) {
                // 计划单货品明细
                List<SalePlanBillGoodsFinal> salePlanBillGoodsFinalList = salePlanBillGoodsFinalDao.selectList(new LambdaQueryWrapper<SalePlanBillGoodsFinal>().eq(SalePlanBillGoodsFinal::getBillId, bill.getId()));
                // 发货单货品明细
                List<SendBillGoods> sendBillGoodsList = sendBillDao.querySendBillGoods(bill.getId(), null);
                Integer endState = 0;
                for (SalePlanBillGoodsFinal goodsFinal : salePlanBillGoodsFinalList) {
                    if (sendBillGoodsList.size() > 0) {
                        SendBillGoods sendBillGoods = sendBillGoodsList.stream().filter(f -> f.getSalePlanId().equals(goodsFinal.getBillId()) && f.getSalePlanGoodsId().equals(goodsFinal.getId())).findFirst().orElse(null);
                        if (sendBillGoods != null) {
                            BigDecimal owqQty = goodsFinal.getQuantity().subtract(sendBillGoods.getQuantity());
                            if (owqQty.compareTo(BigDecimal.ZERO) == 1) {
                                endState++;
                            }
                        } else {
                            endState++;
                        }
                    }
                }
                if (endState == 0) {
                    bill.setFinishFlag(1);
                    bill.setUpdatedTime(new Date());
                    salePlanBillDao.updateById(bill);
                    pedometer++;
                }
            }
            XxlJobHelper.log(String.format("本次完结 销售计划有 %s 单", pedometer));
        }
    }

    @Override
    public void noticeBillAutoComplete(BillParam billParam) {
        Date date = noticeBillDao.queryMinDate();
        List<WeekDate> weekDateList = DateUtil.doDateType(date.getTime(), new Date().getTime());
        for (WeekDate weekDate : weekDateList) {
            // 查询未完结的指令单
            QueryWrapper queryWrapper = new QueryWrapper();
            if (StringUtils.isNotBlank(billParam.getBillNo())) {
                queryWrapper.eq("bill_no", billParam.getBillNo());
            }
            queryWrapper.ne("finish_flag", 1);
            queryWrapper.eq("status", 1);
            queryWrapper.ge("created_time", weekDate.getStartTime());
            queryWrapper.le("created_time", weekDate.getEndTime());
            List<NoticeBill> noticeBillList = noticeBillDao.selectList(queryWrapper);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
            XxlJobHelper.log(String.format("查出 %s ~ %s 未完结 指令单有 %s 单", simpleDateFormat.format(weekDate.getStartTime()), simpleDateFormat.format(weekDate.getEndTime()), noticeBillList.size()));
            Integer pedometer = 0;
            for (NoticeBill bill : noticeBillList) {
                // 指令单货品明细
                List<NoticeBillGoods> goodsList = noticeBillGoodsDao.selectList(new LambdaQueryWrapper<NoticeBillGoods>().eq(NoticeBillGoods::getBillId, bill.getId()));
                // 发货单货品明细
                List<SendBillGoods> sendBillGoodsList = sendBillDao.querySendBillGoods(null, bill.getId());

                Integer endState = 0;
                for (NoticeBillGoods goods : goodsList) {
                    if (sendBillGoodsList.size() > 0) {
                        SendBillGoods sendBillGoods = sendBillGoodsList.stream().filter(f -> f.getNoticeId().equals(goods.getBillId()) && f.getNoticeGoodsId().equals(goods.getId())).findFirst().orElse(null);
                        if (sendBillGoods != null) {
                            BigDecimal owqQty = goods.getQuantity().subtract(sendBillGoods.getQuantity());
                            if (owqQty.compareTo(BigDecimal.ZERO) == 1) {
                                endState++;
                            }
                        } else {
                            endState++;
                        }
                    }
                }
                if (endState == 0) {
                    bill.setFinishFlag(1);
                    bill.setUpdatedTime(new Date());
                    noticeBillDao.updateById(bill);
                    pedometer++;
                }
            }
            XxlJobHelper.log(String.format("本次完结 指令单有 %s 单", pedometer));
        }
    }

    @Override
    public void purchaseBillAutoComplete(BillParam billParam) {
        Date date = purchaseBillDao.queryMinDate();
        List<WeekDate> weekDateList = DateUtil.doDateType(date.getTime(), new Date().getTime());
        for (WeekDate weekDate : weekDateList) {
            // 查询未完结的采购单
            QueryWrapper queryWrapper = new QueryWrapper();
            if (StringUtils.isNotBlank(billParam.getBillNo())) {
                queryWrapper.eq("bill_no", billParam.getBillNo());
            }
            queryWrapper.ne("finish_flag", 1);
            queryWrapper.eq("status", 1);
            queryWrapper.ge("created_time", weekDate.getStartTime());
            queryWrapper.le("created_time", weekDate.getEndTime());
            List<PurchaseBill> purchaseBillList = purchaseBillDao.selectList(queryWrapper);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
            XxlJobHelper.log(String.format("查出 %s ~ %s 未完结 采购单 %s 单", simpleDateFormat.format(weekDate.getStartTime()), simpleDateFormat.format(weekDate.getEndTime()), purchaseBillList.size()));
            Integer pedometer = 0;
            for (PurchaseBill bill : purchaseBillList) {
                // 采购单调后货品明细
                List<PurchaseBillGoodsFinal> goodsFinalList = purchaseBillGoodsFinalDao.selectList(new LambdaQueryWrapper<PurchaseBillGoodsFinal>().eq(PurchaseBillGoodsFinal::getBillId, bill.getId()));
                // 采购入库单
                List<PurchaseReceiveBill> purchaseReceiveBillList = purchaseReceiveBillDao.selectList(
                        new LambdaQueryWrapper<PurchaseReceiveBill>().eq(BillMasterData::getStatus, 1).eq(PurchaseReceiveBill::getPurchaseId, bill.getId()));
                List<Long> purchaseReceiveBillId = purchaseReceiveBillList.stream().map(BillMasterData::getId).collect(Collectors.toList());
                Integer endState = 0;
                if (purchaseReceiveBillId.size() > 0) {
                    List<PurchaseReceiveBillGoods> purchaseReceiveBillGoodsList = purchaseReceiveBillGoodsDao.selectList(
                            new LambdaQueryWrapper<PurchaseReceiveBillGoods>().in(PurchaseReceiveBillGoods::getBillId,purchaseReceiveBillId));

                    for (PurchaseBillGoodsFinal goods : goodsFinalList) {
                        PurchaseReceiveBillGoods purchaseReceiveBillGoods = purchaseReceiveBillGoodsList.stream().filter(f -> f.getGoodsId().equals(goods.getGoodsId())).findFirst().orElse(null);
                        if (purchaseReceiveBillGoods != null) {
                            BigDecimal owqQty = goods.getQuantity().subtract(purchaseReceiveBillGoods.getQuantity());
                            if (owqQty.compareTo(BigDecimal.ZERO) == 1) {
                                endState++;
                            }
                        } else {
                            endState++;
                        }
                    }
                }
                if (endState == 0) {
                    bill.setFinishFlag(1);
                    bill.setUpdatedTime(new Date());
                    purchaseBillDao.updateById(bill);
                    pedometer++;
                }
            }
            XxlJobHelper.log(String.format("本次完结 采购单 有 %s 单", pedometer));
        }
    }


}
