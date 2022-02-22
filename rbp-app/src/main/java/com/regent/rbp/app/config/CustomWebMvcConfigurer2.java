package com.regent.rbp.app.config;

import com.regent.rbp.api.web.config.AuthHandlerInterceptor;
import com.regent.rbp.app.RbpAppApplication;
import com.regent.rbp.app.adapter.CustomWebMvcConfigurer;
import com.regent.rbp.infrastructure.constants.ResourceConstant;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * copy from com.regent.rbp.app.adapter.CustomWebMvcConfigurer,
 * but modify
 * @see CustomWebMvcConfigurer#getValidator() to retutrn null and replace it with the
 * @see CustomWebMvcConfigurer2#validator()
 * othe changes is about the @ComponentScan aboute main class
 * @see RbpAppApplication
 *
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
@Configuration
public class CustomWebMvcConfigurer2 implements WebMvcConfigurer {
    static Logger logger = LoggerFactory.getLogger(CustomWebMvcConfigurer.class);
    @Autowired
    private AuthHandlerInterceptor authHandlerInterceptor;

    /**
     * 上传目录
     */
    @Value("${file.uploadFolder:''}")
    private String uploadFolder;

    @Value("${file.staticAccessPath:'/webFile/**'}")
    private String staticAccessPath;

    @Value("${rbp.disableJwt:false}")
    private boolean disableJwt;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        registry.addResourceHandler(staticAccessPath)
                .addResourceLocations("file:" + uploadFolder);
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger/**")
                .addResourceLocations("classpath:/static/swagger/");

        //设置上传路径
        String currentUploadFolder = getUploadFolder();
        String staticAccessPath = ResourceConstant.staticAccessPath;
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + currentUploadFolder);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        multipartResolver.setMaxUploadSize(50 * 1024 * 1024); //上传文件大小 50M
        multipartResolver.setMaxInMemorySize(1048576);
        return multipartResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!disableJwt) {
            registry.addInterceptor(authHandlerInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
        }
    }

    private String getUploadFolder() {
        String currentUploadFolder = uploadFolder;
        if (StringUtil.isEmpty(uploadFolder)) {
            currentUploadFolder = System.getProperty("user.dir");
        }
        logger.info("uploadFolder = " + currentUploadFolder);
        return currentUploadFolder;
    }

    /**
     * 国际化 start
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        //自定义参数
        localeChangeInterceptor.setParamName(ResourceConstant.LANG);
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        //指定默认语言为中文
        Locale locale = new Locale("zh", "chs");
        localeResolver.setDefaultLocale(locale);
        return localeResolver;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    /**
     * link to resources/language-->Bundle 'message'
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:validation/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    /**
     * require for clean old exist validator!!! don't delete
     * @see com.regent.rbp.app.adapter.CustomWebMvcConfigurer#getValidator
     *
     * @return
     */
    @Override
    public Validator getValidator() {
        return null;
    }

}
