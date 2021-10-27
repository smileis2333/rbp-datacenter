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
     * 注册标准接口
     *
     * @return
     */
    @Bean
    public Docket datacenterStandardApi() {
        String moduleCode = "dateCenterStandard";
        String moduleName = "数据中台标准接口";
        String basePackage = "com.regent.rbp.api";
        return Swagger2Configuration.docket(moduleCode, moduleName, basePackage);
    }

    /**
     * 注册INNO接口
     *
     * @return
     */
    @Bean
    public Docket datacenterInnoApi() {
        String moduleCode = "dateCenterInno";
        String moduleName = "数据中台INNO接口";
        String basePackage = "com.regent.rbp.task.inno";
        return Swagger2Configuration.docket(moduleCode, moduleName, basePackage);
    }
}

