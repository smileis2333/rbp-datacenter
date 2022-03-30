package com.regent.rbp.task.standard.job;

import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.standard.service.TaskChannelNewGoodsGuardService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 计算渠道旧品定时任务
 * @author Moruins
 * @date 2022-03-29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskChannelNewGoodsGuardJob {

    private final TaskChannelNewGoodsGuardService taskChannelNewGoodsGuardService;

    @XxlJob(SystemConstants.TASK_CHANNEL_NEW_GOODS_GUARD_JOB)
    public void taskNewGoodsHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            taskChannelNewGoodsGuardService.runJob();
        } catch (Exception e) {
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }
}
