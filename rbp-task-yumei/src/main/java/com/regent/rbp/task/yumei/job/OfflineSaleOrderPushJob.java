package com.regent.rbp.task.yumei.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.FeignException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
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
                "and (select count(*) from rbp_sales_order_bill_size rsobs2 where rsobs2.bill_id = rsob.id) >0");
        querySql = limitQuery(querySql, "check_time");
        querySql += " order by rsob.check_time asc";
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
                        "values(%s,'%s','%s','%s','%s')", billId, billNo, "rbp_sales_order_bill", DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
                dbDao.insert(insertStatement);
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei success", billId, billNo));
            } catch (FeignException e) {
                String msg = String.format("billId: %s, billNo: %s push yumei fail, please see `rbp_third_party_invoke_log` table", billId, billNo);
                log.error(msg);
                e.printStackTrace();
                XxlJobHelper.log(msg);
            } catch (Exception e) {
                String msg = String.format("billId: %s, billNo: %s push yumei fail, msg %s", billId, billNo, e.getMessage());
                log.error(msg);
                e.printStackTrace();
                XxlJobHelper.log(msg);
            }

        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    public String limitQuery(String rawSql, String timeField) {
        String param = XxlJobHelper.getJobParam();
        if (StrUtil.isBlank(param)) {
            return rawSql;
        }
        try {
            TimeRange timeRange = objectMapper.readValue(param, TimeRange.class);
            LocalDate startDate = timeRange.getStartDate();
            LocalDate endDate = timeRange.getEndDate();
            if (startDate != null && endDate != null) {
                rawSql += String.format(" and (%s >= '%s' and %s <= '%s') ", timeField, startDate, timeField, endDate);
            } else if (startDate != null) {
                rawSql += String.format(" and %s >= '%s' ", timeField, startDate);
            } else if (endDate != null) {
                rawSql += String.format(" and %s <= '%s' ", timeField, endDate);
            }
        } catch (IOException e) {
            String msg = String.format("time param format error, fail to process %s", rawSql);
            log.error(msg);
            XxlJobHelper.log(msg);
            throw new RuntimeException(e);
        }
        return rawSql;
    }

}

@Data
class TimeRange {
    private LocalDate startDate;
    private LocalDate endDate;
}
