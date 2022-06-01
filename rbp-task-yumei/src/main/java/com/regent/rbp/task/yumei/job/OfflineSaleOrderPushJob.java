package com.regent.rbp.task.yumei.job;

import cn.hutool.core.date.DateUtil;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 销售单推送（玉美）
 * @author: HaiFeng
 * @create: 2022/5/27 10:43
 */
@Slf4j
@Component
public class OfflineSaleOrderPushJob {

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private DbDao dbDao;

    /**
     * 推送销售单到玉美
     * 请求Json：{ "billNo": "123,456" }
     */
    @XxlJob(SystemConstants.CREATE_OFFLINE_SALE_ORDER)
    public void orderPushRetryHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);

        // 未推送且货品明细都已设置条形码
        String querySql = String.format("select id,bill_no from rbp_sales_order_bill rsob where status = 1 and not exists (select * from yumei_push_log ypl where ypl.bill_id = rsob.id and ypl.target_table = 'rbp_sales_order_bill')" +
                "and (select count(*) from rbp_sales_order_bill_size rsobs2 where rsobs2.bill_id = rsob.id) >0" +
                " order by rsob.check_time asc");
        List<Map<String, Object>> params = dbDao.selectList(querySql);
        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            Date startTime = new Date();
            try {
                saleOrderService.createOfflineSaleOrder(billNo);
                Date endTime = new Date();
                String insertStatement = String.format("" +
                        "insert\n" +
                        "\tinto\n" +
                        "\tyumei_push_log(\n" +
                        "\tbill_id,\n" +
                        "\tbill_no,\n" +
                        "\ttarget_table,\n" +
                        "\tpush_start_time,\n" +
                        "\tpush_end_time)\n" +
                        "values(%s,'%s','%s','%s','%s')", billId, billNo, "rbp_sales_order_bill", DateUtil.format(startTime, "yyyy-MM-dd"), DateUtil.format(endTime, "yyyy-MM-dd"));
                dbDao.insert(insertStatement);
            } catch (FeignException e) {
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei fail, please see `rbp_third_party_invoke_log` table", billId, billNo));
            } catch (Exception e) {
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei fail, msg %s", billId, billNo, e.getMessage()));
            }

        }
    }

}
