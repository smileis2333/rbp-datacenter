package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.retail.RetailOrderPushLog;
import com.regent.rbp.api.dao.base.BaseDbDao;
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
 * @author liuzhicheng
 * @createTime 2022-04-09
 * @Description
 */
@Slf4j
@Component
public class OrderPushRetryJob {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private BaseDbDao baseDbDao;

    /**
     * 推送订单到玉美失败重试
     * 请求Json：{ "billNo": "123456,2333333" }
     */
    @XxlJob(SystemConstants.ORDER_PUSH_RETRY_JOB)
    public void orderPushRetryHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            List<String> billNoList = new ArrayList<>();
            // 读取参数(单号)
            String param = XxlJobHelper.getJobParam();
            RetailOrderPushLog retailOrderPushLog = JSON.parseObject(param, RetailOrderPushLog.class);
            if (null != retailOrderPushLog && StrUtil.isNotEmpty(retailOrderPushLog.getBillNo())) {
                billNoList.addAll(Arrays.asList(retailOrderPushLog.getBillNo().split(StrUtil.COMMA)));
            } else {
                // 一天内，重试次数不超过3次
                StringBuilder sb = new StringBuilder();
                sb.append("select bill_no from rbp_retail_order_push_log a \n");
                sb.append("where sucess = 0 and created_time > sysdate() - 1 \n");
                sb.append("and not exists (select 1 from rbp_retail_order_push_log b where b.sucess = 1 and a.bill_no = b.bill_no)\n");
                sb.append("group by bill_no having count(*) < 3");
                List<String> list = baseDbDao.getStringListDataBySql(sb.toString());
                if (CollUtil.isNotEmpty(list)) {
                    for (String billNo : list) {
                        billNoList.addAll(Arrays.asList(billNo.split(StrUtil.COMMA)));
                    }
                }
            }

            if (CollUtil.isNotEmpty(billNoList)) {
                String errorMsg = saleOrderService.pushOrderToYuMei(billNoList);
                if (StrUtil.isNotEmpty(errorMsg)) {
                    XxlJobHelper.log(errorMsg);
                    XxlJobHelper.handleFail(errorMsg);
                }
            }
        } catch (Exception e) {
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }
}
