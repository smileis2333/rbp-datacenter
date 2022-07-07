package com.regent.rbp.task.yumei.job;


import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.service.OffshopService;
import com.regent.rbp.task.yumei.service.impl.YumeiPushService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ChannelTuneInPushJob {
    @Autowired
    private OffshopService offshopService;
    @Autowired
    private YumeiPushService yumeiPushService;

    /**
     * 推送渠道调出单
     */
    @XxlJob("yumei.pushChannelTuneIn")
    public void orderPushRetryHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        List<Map<String, Object>> params = yumeiPushService.getWaitPushBill("rbp_receive_bill", "701016");

        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            yumeiPushService.processPush(billId, billNo, offshopService::checkChannelTuneIn, "rbp_receive_bill");
        }
    }

}
