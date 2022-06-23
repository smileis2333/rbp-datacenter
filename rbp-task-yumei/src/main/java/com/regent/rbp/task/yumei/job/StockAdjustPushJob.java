package com.regent.rbp.task.yumei.job;

import cn.hutool.core.date.DateUtil;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.task.yumei.service.OffshopService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StockAdjustPushJob {
    @Autowired
    private OffshopService offshopService;
    @Autowired
    private DbDao dbDao;

    @XxlJob("yumei.stockAdjustPushHandler")
    public void stockAdjustPushHandler() {
        // 未推送且货品明细都已设置条形码
        String querySql = "select id,bill_no,check_time from rbp_stock_adjust_bill rsab where status  = 1 and not exists (select * from yumei_push_log ypl where ypl.bill_id = rsab.id) order by check_time asc";
        List<Map<String, Object>> params = dbDao.selectList(querySql);
        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            Date startTime = new Date();
            try {
                offshopService.checkStockAdjust(billNo);
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
                        "values(%s,'%s','%s','%s','%s')", billId, billNo, "rbp_stock_adjust_bill", DateUtil.format(startTime, "yyyy-MM-dd"), DateUtil.format(endTime, "yyyy-MM-dd"));
                dbDao.insert(insertStatement);
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei success", billId, billNo));
            } catch (FeignException e) {
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei fail, please see `rbp_third_party_invoke_log` table", billId, billNo));
            } catch (Exception e) {
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei fail, msg %s", billId, billNo, e.getMessage()));
            }

        }

    }
}
