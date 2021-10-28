package com.regent.rbp.api.service.job;

import com.regent.rbp.api.service.base.ServiceProcessor;

/**
 * 处理模块事件，将模块事件根据订阅设置转发到Task任务中
 * @author xuxing
 */
public class ModuleEventServiceProcessor implements ServiceProcessor {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
