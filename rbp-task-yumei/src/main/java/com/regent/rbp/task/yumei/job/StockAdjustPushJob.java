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
        List<Map<String, Object>> params = yumeiPushService.getWaitPushBill("rbp_stock_adjust_bill", "701022");
        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            yumeiPushService.processPush(billId, billNo, offshopService::checkStockAdjust, "rbp_stock_adjust_bill");
        }

    }
}
