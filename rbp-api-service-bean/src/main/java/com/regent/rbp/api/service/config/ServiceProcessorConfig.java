package com.regent.rbp.api.service.config;

import com.regent.rbp.api.service.base.ServiceProcessor;
import com.regent.rbp.api.service.job.ModuleEventServiceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * 注册服务处理器
 * @author xuxing
 */
public class ServiceProcessorConfig {
    private final static Logger logger = LoggerFactory.getLogger(ServiceProcessorConfig.class);

    @Bean
    public ServiceProcessor moduleEventServiceProcessor() {
        ServiceProcessor serviceProcessor = new ModuleEventServiceProcessor();
//        logger.info("正在启动模块事件处理器....");
//        serviceProcessor.start();
//        logger.info("启动模块事件处理器成功....");
        return serviceProcessor;
    }
}
