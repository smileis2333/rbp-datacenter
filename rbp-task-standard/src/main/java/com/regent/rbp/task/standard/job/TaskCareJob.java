package com.regent.rbp.task.standard.job;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.standard.module.param.TaskCareParam;
import com.regent.rbp.task.standard.service.TaskCareService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description 任务中心-关怀任务
 */
@Slf4j
@Component
public class TaskCareJob {

    @Autowired
    private TaskCareService taskCareService;

    /**
     * 关怀任务处理回访会员列表
     * 请求Json：{ "taskCodeList": ["",""] }
     */
    @XxlJob(SystemConstants.TASK_CARE_MEMBER_HANDLER)
    public void taskCareMemberHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            // 读取参数(关怀任务编号)
            String param = XxlJobHelper.getJobParam();
            TaskCareParam taskCareParam = JSON.parseObject(param, TaskCareParam.class);
            String errorMsg = taskCareService.taskCareMemberHandler(taskCareParam);
            if (StrUtil.isNotEmpty(errorMsg)) {
                XxlJobHelper.log(errorMsg);
                XxlJobHelper.handleFail(errorMsg);
                log.error(errorMsg);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }

    /**
     * 关怀任务处理渠道列表
     * 请求Json：{ "taskCodeList": ["",""] }
     */
    @XxlJob(SystemConstants.TASK_CARE_CHANNEL_HANDLER)
    public void taskCareChannelHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            // 读取参数(关怀任务编号)
            String param = XxlJobHelper.getJobParam();
            TaskCareParam taskCareParam = JSON.parseObject(param, TaskCareParam.class);
            String errorMsg = taskCareService.taskCareChannelHandler(taskCareParam);
            if (StrUtil.isNotEmpty(errorMsg)) {
                XxlJobHelper.log(errorMsg);
                XxlJobHelper.handleFail(errorMsg);
                log.error(errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }
}
