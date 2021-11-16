package com.regent.rbp.task.standard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.dao.salePlan.SalePlanBillDao;
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
    BillOweService billOweService;

    @Override
    public void salePlanBillAutoComplete(String billNo) {
        // 查询未完结的销售计划
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(billNo)) {
            queryWrapper.eq("bill_no", billNo);
        }
        queryWrapper.ne("finish_flag", 1);
        List<SalePlanBill> salePlanBillList = salePlanBillDao.selectList(queryWrapper);
        XxlJobHelper.log(String.format("查出 未完结 销售计划有 %s 单", salePlanBillList.size()));
        Integer pedometer = 0;
        for (SalePlanBill bill : salePlanBillList) {
            List<SalePlanBillOweDetail> detail = billOweService.calculateSalePlanBillOwe(null, null, null, null, bill.getId());
            if (detail == null){
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
        // 查询未完结的销售计划
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(billNo)) {
            queryWrapper.eq("bill_no", billNo);
        }
        queryWrapper.ne("finish_flag", 1);
        List<SalePlanBill> salePlanBillList = salePlanBillDao.selectList(queryWrapper);
        XxlJobHelper.log(String.format("查出 未完结 指令单有 %s 单", salePlanBillList.size()));
        Integer pedometer = 0;
        for (SalePlanBill bill : salePlanBillList) {
            List<SalePlanBillOweDetail> detail = billOweService.calculateSalePlanBillOwe(null, null, null, null, bill.getId());
            if (detail == null){
                bill.setFinishFlag(1);
                bill.setUpdatedTime(new Date());
                salePlanBillDao.updateById(bill);
                pedometer++;
            }
        }
        XxlJobHelper.log(String.format("本次完结 指令单有 %s 单", pedometer));
    }


}
