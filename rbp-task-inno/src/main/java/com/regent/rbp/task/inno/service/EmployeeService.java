package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.model.resp.EmployeeRespDto;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
public interface EmployeeService{
    OnlinePlatform getOnlinePlatform(String onlinePlatformCode);

    void uploadingEmployee(Long id, Long channelId);

}
