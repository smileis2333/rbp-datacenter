package com.regent.rbp.app.config.dev;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

@Configuration
public class DispatchConfig {

    @Bean
    @Primary
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet,
                                                          WebMvcProperties webMvcProperties,
                                                          ObjectProvider<MultipartConfigElement> multipartConfig) {
        DispatcherServletRegistrationBean registration = new DispatcherServletRegistrationBean(dispatcherServlet, webMvcProperties.getServlet().getPath());
        registration.setName("dispatcherServlet");
        registration.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        multipartConfig.ifAvailable(registration::setMultipartConfig);
        return registration;
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet(WebMvcProperties webMvcProperties) {
        LoggableDispatcherServlet dispatcherServlet = new LoggableDispatcherServlet();
        dispatcherServlet.setDispatchOptionsRequest(webMvcProperties.isDispatchOptionsRequest());
        dispatcherServlet.setDispatchTraceRequest(webMvcProperties.isDispatchTraceRequest());
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(webMvcProperties.isThrowExceptionIfNoHandlerFound());
//        dispatcherServlet.setPublishEvents(webMvcProperties.isPublishRequestHandledEvents());
//        dispatcherServlet.setEnableLoggingRequestDetails(webMvcProperties.isLogRequestDetails());
        return dispatcherServlet;
    }

}