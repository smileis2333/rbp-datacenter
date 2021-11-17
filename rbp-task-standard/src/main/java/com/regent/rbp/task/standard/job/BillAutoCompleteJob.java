package com.regent.rbp.task.standard.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.standard.module.param.BillParam;
import com.regent.rbp.task.standard.service.BillAutoCompleteService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @program: rbp-datacenter
 * @description: 单据自动完结
 * @author: HaiFeng
 * @create: 2021-11-15 16:58
 */
@Slf4j
@Component
public class BillAutoCompleteJob {

    @Autowired
    BillAutoCompleteService billAutoCompleteService;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 自动完结销售计划
     * 请求Json：{ "billNo": "" }
     */
    @XxlJob(SystemConstants.SALE_PLAN_BILL_AUTO_COMPLETE)
    public void salePlanBillAutoComplete() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            BillParam billParam = JSON.parseObject(param, BillParam.class);
            billAutoCompleteService.salePlanBillAutoComplete(billParam);
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

    /**
     * 自动完结指令单
     * 请求Json：{ "billNo": "" }
     */
    @XxlJob(SystemConstants.NOTICE_BILL_AUTO_COMPLETE)
    public void noticeBillAutoComplete() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            BillParam billParam = JSON.parseObject(param, BillParam.class);
            billAutoCompleteService.noticeBillAutoComplete(billParam);
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

    /**
     * 自动完结采购单
     * 请求Json：{ "billNo": "" }
     */
    @XxlJob(SystemConstants.PURCHASE_BILL_AUTO_COMPLETE)
    public void purchaseBillAutoComplete() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            BillParam billParam = JSON.parseObject(param, BillParam.class);
            billAutoCompleteService.purchaseBillAutoComplete(billParam);
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }
}
