package com.regent.rbp.task.yumei.job;

import com.regent.rbp.task.yumei.service.OffshopService;
import com.regent.rbp.task.yumei.service.impl.YumeiPushService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StockAdjustPushJob {
    @Autowired
    private OffshopService offshopService;
    @Autowired
    private YumeiPushService yumeiPushService;

    @XxlJob("yumei.stockAdjustPushHandler")
    public void stockAdjustPushHandler() {
        // constant query, filter business type
        String hookSql = "and business_type_id  = (select id  from rbp_business_type rbt where base_business_type_id  = 1100000000000032)";
        List<Map<String, Object>> params = yumeiPushService.getWaitPushBill("rbp_stock_adjust_bill", "701022", hookSql);
        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            yumeiPushService.processPush(billId, billNo, offshopService::checkStockAdjust, "rbp_stock_adjust_bill");
        }

    }
}
