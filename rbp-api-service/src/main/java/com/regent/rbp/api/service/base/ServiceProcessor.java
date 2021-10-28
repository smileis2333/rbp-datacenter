package com.regent.rbp.api.service.base;

/**
 * @author xuxing
 */
public interface ServiceProcessor {

    /**
     * 开启服务处理器
     *
     * @param
     * @return
     */
    void start();


    /**
     * 关闭服务处理器
     *
     * @param
     * @return
     */
    void stop();

    /**
     * 服务处理器开启状态
     *
     * @param
     * @return
     */
    boolean isRunning();
}
