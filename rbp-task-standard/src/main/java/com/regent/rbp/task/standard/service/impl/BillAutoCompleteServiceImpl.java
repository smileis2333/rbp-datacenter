package com.regent.rbp.task.standard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.BillMasterData;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoods;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoodsFinal;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.dao.noticeBill.NoticeBillDao;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillGoodsDao;
import com.regent.rbp.api.dao.salePlan.SalePlanBillGoodsFinalDao;
import com.regent.rbp.api.dao.sendBill.SendBillDao;
import com.regent.rbp.api.dao.sendBill.SendBillGoodsDao;
import com.regent.rbp.api.dto.calculate.NoticeBillOweDetail;
import com.regent.rbp.api.dto.calculate.PurchaseBillOweDetail;
import com.regent.rbp.api.dto.calculate.SalePlanBillOweDetail;
import com.regent.rbp.api.service.calculate.BillOweService;
import com.regent.rbp.task.standard.service.BillAutoCompleteService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    SendBillDao sendBill;
    @Autowired
    SendBillGoodsDao sendBillGoodsDao;
    @Autowired
    PurchaseBillDao purchaseBillDao;
    @Autowired
    BillOweService billOweService;

    @Override
    public void salePlanBillAutoComplete(String billNo) {
        // 查询未完结的计划单
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(billNo)) {
            queryWrapper.eq("bill_no", billNo);
        }
        queryWrapper.ne("finish_flag", 1);
        queryWrapper.eq("status", 1);
        List<SalePlanBill> salePlanBillList = salePlanBillDao.selectList(queryWrapper);
        XxlJobHelper.log(String.format("查出 未完结 计划单 %s 单", salePlanBillList.size()));
        Integer pedometer = 0;
        for (SalePlanBill bill : salePlanBillList) {
            List<SalePlanBillOweDetail> detail = billOweService.calculateSalePlanBillOwe(null, null, null, null, bill.getId());
            if (detail != null && detail.size() == 0){
                bill.setFinishFlag(1);
                bill.setUpdatedTime(new Date());
                salePlanBillDao.updateById(bill);
                pedometer++;
            }
        }
        XxlJobHelper.log(String.format("本次完结 销售计划有 %s 单", pedometer));
    }

    @Override
    public void noticeBillAutoComplete(String billNo) {
        // 查询未完结的指令单
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(billNo)) {
            queryWrapper.eq("bill_no", billNo);
        }
        queryWrapper.ne("finish_flag", 1);
        List<NoticeBill> noticeBillList = noticeBillDao.selectList(queryWrapper);
        XxlJobHelper.log(String.format("查出 未完结 指令单有 %s 单", noticeBillList.size()));
        Integer pedometer = 0;
        for (NoticeBill bill : noticeBillList) {
            List<NoticeBillOweDetail> detail = billOweService.calculateNoticeBillOwe(null, null, null, null, bill.getId());
            if (detail != null && detail.size() == 0){
                bill.setFinishFlag(1);
                bill.setUpdatedTime(new Date());
                noticeBillDao.updateById(bill);
                pedometer++;
            }
        }
        XxlJobHelper.log(String.format("本次完结 指令单有 %s 单", pedometer));
    }

    @Override
    public void purchaseBillAutoComplete(String billNo) {
        // 查询未完结的采购单
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(billNo)) {
            queryWrapper.eq("bill_no", billNo);
        }
        queryWrapper.ne("finish_flag", 1);
        List<PurchaseBill> purchaseBillList = purchaseBillDao.selectList(queryWrapper);
        XxlJobHelper.log(String.format("查出 未完结 采购单 有 %s 单", purchaseBillList.size()));
        Integer pedometer = 0;
        for (PurchaseBill bill : purchaseBillList) {
            List<PurchaseBillOweDetail> detail = billOweService.calculatePurchaseBillOwe(null, null, null, null, bill.getId());
            if (detail != null && detail.size() == 0){
                bill.setFinishFlag(1);
                bill.setUpdatedTime(new Date());
                purchaseBillDao.updateById(bill);
                pedometer++;
            }
        }
        XxlJobHelper.log(String.format("本次完结 采购单 有 %s 单", pedometer));
    }


}
