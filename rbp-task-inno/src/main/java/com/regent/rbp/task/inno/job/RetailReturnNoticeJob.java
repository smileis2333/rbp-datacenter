package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知 Job
 * @author: HaiFeng
 * @create: 2021-10-13 18:29
 */
@Slf4j
@Component
public class RetailReturnNoticeJob {

    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "[inno拉取全渠道退货通知列表]:电商平台编号不存在";

    @Autowired
    RetailReturnNoticeService retailReturnNoticeService;
    @Autowired
    OnlinePlatformService onlinePlatformService;

    /**
     * 拉取全渠道退货通知单列表
     * 入参格式：{ "onlinePlatformCode": "INNO", "orderSn": "", "returnSn": "THD000532110141327194170111" }
     * 入参格式：{ "onlinePlatformCode": "INNO", "returnSn": "THD000532110141327194170111" }
     * onlinePlatformCode：平台编号
     * orderSn：订单号
     * returnSn：退货单号
     */
    @XxlJob(SystemConstants.GET_APP_RETURN_ORDER)
    public void downloadRetailReturnNoticeListJobHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            RetailReturnNoticeParam retailReturnNoticeParam = JSON.parseObject(param, RetailReturnNoticeParam.class);
            OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(retailReturnNoticeParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            retailReturnNoticeService.downloadRetailReturnNoticeList(retailReturnNoticeParam);
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

}
