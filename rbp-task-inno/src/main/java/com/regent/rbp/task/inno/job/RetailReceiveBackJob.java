package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.param.UpdateRetailReceiveBackByStatusParam;
import com.regent.rbp.task.inno.service.RetailReceiveBackService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: rbp-datacenter
 * @description: 全渠道收退货单 job
 * @author: HaiFeng
 * @create: 2021-10-14 17:51
 */
@Slf4j
@Component
public class RetailReceiveBackJob {

    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "[inno更新退货单收货状态]:电商平台编号不存在";

    @Autowired
    RetailReceiveBackService retailReceiveBackService;
    @Autowired
    OnlinePlatformService onlinePlatformService;

    /**
     * 更新退货单收货状态
     * 入参格式：{ "onlinePlatformCode": "INNO" }
     * onlinePlatformCode：平台编号
     */
    @XxlJob(SystemConstants.POST_RETURN_ORDER_STATUS)
    public void UpdateReturnOrderStatus() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            UpdateRetailReceiveBackByStatusParam updateRetailReceiveBackByStatusParam = JSON.parseObject(param, UpdateRetailReceiveBackByStatusParam.class);
            OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(updateRetailReceiveBackByStatusParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            retailReceiveBackService.UpdateReturnOrderStatus(updateRetailReceiveBackByStatusParam);
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

}
