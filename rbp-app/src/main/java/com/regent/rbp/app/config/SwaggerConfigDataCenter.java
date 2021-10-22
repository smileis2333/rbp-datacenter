package com.regent.rbp.app.config;

import com.regent.rbp.infrastructure.swagger.Swagger2Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author xuxing
 */
@Configuration
public class SwaggerConfigDataCenter {
    /**
     * 注册Swagger
     *
     * @return
     */
    @Bean
    public Docket datacenterStandardApi() {
        String moduleCode = "dateCenterStandard";
        String moduleName = "数据中台标准接口服务";
        String basePackage = "com.regent.rbp.api";
        return Swagger2Configuration.docket(moduleCode, moduleName, basePackage);
    }

    /**
     * 注册Swagger
     *
     * @return
     */
    @Bean
    public Docket datacenterPlatformApi() {
        String moduleCode = "dateCenterPlatform";
        String moduleName = "数据中台平台接口服务";
        String basePackage = "com.regent.rbp.task";
        return Swagger2Configuration.docket(moduleCode, moduleName, basePackage);
    }
}

