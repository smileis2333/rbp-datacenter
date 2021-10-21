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
    public Docket datacenterApi() {
        String moduleCode = "dateCenter";
        String moduleName = "数据中台服务";
        //String basePackage = "com.regent.rbp.api";
        String basePackage = "com.regent.rbp.task";
        return Swagger2Configuration.docket(moduleCode, moduleName, basePackage);
    }
}

