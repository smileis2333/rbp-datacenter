package com.regent.rbp.task.standard.service;

import com.regent.rbp.task.standard.module.param.TaskCareParam;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description
 */
public interface TaskCareService {

    /**
     * 关怀任务处理回访会员列表
     *
     * @param taskCareParam 任务编号，为空处理全部
     * @return 错误信息
     */
    String taskCareMemberHandler(TaskCareParam taskCareParam);

    /**
     * 关怀任务处理渠道列表
     *
     * @param taskCareParam 任务编号，为空处理全部
     * @return 错误信息
     */
    String taskCareChannelHandler(TaskCareParam taskCareParam);
}
