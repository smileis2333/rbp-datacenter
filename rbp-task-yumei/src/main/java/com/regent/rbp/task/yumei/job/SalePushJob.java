package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.retail.RetailOrderPushLog;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillPushLog;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单推送（玉美）
 * @author: HaiFeng
 * @create: 2022/5/27 10:43
 */
@Slf4j
@Component
public class SalePushJob {

    @Autowired
    private SalesOrderBillDao salesOrderBillDao;
    @Autowired
    private SaleOrderService saleOrderService;


    /**
     * 推送销售单到玉美
     * 请求Json：{ "billNo": "123,456" }
     */
    @XxlJob(SystemConstants.CREATE_OFFLINE_SALE_ORDER)
    public void orderPushRetryHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            List<String> saleList = new ArrayList<>();
            // 读取参数(单号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            SalesOrderBillPushLog salePushLog = JSON.parseObject(param, SalesOrderBillPushLog.class);
            if (null != salePushLog && StrUtil.isNotEmpty(salePushLog.getBillNo())) {
                saleList.addAll(Arrays.asList(salePushLog.getBillNo().split(StrUtil.COMMA)));
            } else {
                saleList = salesOrderBillDao.getNotPushSale();
            }

            if (CollUtil.isNotEmpty(saleList)) {
                saleList.stream().forEach(item -> {
                    String errorMsg = saleOrderService.createOfflineSaleOrder(item);
                    if (StrUtil.isNotEmpty(errorMsg)) {
                        XxlJobHelper.log(errorMsg);
                        XxlJobHelper.handleFail(errorMsg);
                    }
                });

            }


        } catch (Exception e) {
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }

}
