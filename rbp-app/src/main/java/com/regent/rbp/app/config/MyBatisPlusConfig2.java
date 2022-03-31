package com.regent.rbp.app.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @see MyBatisPlusConfig
 * 由于直接注入了接近根部的配置类，MybatisSqlSessionFactoryBean，
 * 触发了com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration#sqlSessionFactory(javax.sql.DataSource)的@ConditionalOnMissingBean，且未没有对原自动配置类的机制进行仿写
 * 导致原子节点的自动配置机制(@Bean等相关自动配置)包括且不限于GlobalConfig全部失效
 * 正确的做法应为通过两个回调类
 * @see ConfigurationCustomizer
 * @see MybatisPlusPropertiesCustomizer
 * 进行插拔信息配置
 * 为保留最低修改程度，此处并未进行任何改动，仅新增数行
 * 内容复制原本的类内容，并注入了GlobalConfig的ID生成器，放置未设置字典导致其默认雪花ID长度过长导致的系统结构不兼容(前端显示精度)
 */
@Configuration
@MapperScan(basePackages = "com.regent.rbp.**.dao")
public class MyBatisPlusConfig2 {

    @Value("${mybatis-plus.typeAliasesPackage:com.regent.rbp.**.model}")
    private String typeAliasesPackage;

    @Value("${mybatis-plus.mapperLocations:classpath*:mappers/**/*Mapper.xml}")
    private String mapperLocations;

    @Value("${mybatis-plus.configuration.local-cache-scope:statement}")
    private String localCacheScope;

    @Value("${mybatis-plus.configuration.map-underscore-to-camel-case:false}")
    private Boolean mapUnderscoreToCamelCase;

    @Value("${mybatis-plus.configuration.cache-enabled:false}")
    private Boolean cacheEnabled;


    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    @Autowired
    @Qualifier("dynamicDataSource")
    private DataSource dataSource;

    @Bean("sqlSessionFactory")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws Exception {
        typeAliasesPackage = setTypeAliasesPackage(typeAliasesPackage);
        VFS.addImplClass(SpringBootVFS.class);
        PaginationInterceptor pageInter = new PaginationInterceptor();
        pageInter.setDialectType("mysql");
        //zyh 2020-04-21 考虑全量导出情况，拦截器默认不设置每页条数限制
        pageInter.setLimit(-1L);
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sessionFactory.setPlugins(new PaginationInterceptor[]{pageInter});

        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setCacheEnabled(cacheEnabled);
        if (LocalCacheScope.STATEMENT.toString().toLowerCase().equals(localCacheScope)) {
            mybatisConfiguration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        }
        mybatisConfiguration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        mybatisConfiguration.setCallSettersOnNulls(true);
        mybatisConfiguration.setDefaultExecutorType(ExecutorType.SIMPLE);
        // 配置打印sql语句
        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        sessionFactory.setConfiguration(mybatisConfiguration);

        // 新增部分-->ID生成器
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setIdentifierGenerator(new CustomerIdGenerator());
        sessionFactory.setGlobalConfig(globalConfig);
        return sessionFactory;
    }

    public static String setTypeAliasesPackage(String typeAliasesPackage) {
        ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        List<String> allResult = new ArrayList<String>();
        try {
            for (String aliasesPackage : typeAliasesPackage.split(",")) {
                List<String> result = new ArrayList<String>();
                aliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        + ClassUtils.convertClassNameToResourcePath(aliasesPackage.trim()) + "/" + DEFAULT_RESOURCE_PATTERN;
                Resource[] resources = resolver.getResources(aliasesPackage);
                if (resources != null && resources.length > 0) {
                    MetadataReader metadataReader = null;
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            try {
                                result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (result.size() > 0) {
                    HashSet<String> hashResult = new HashSet<String>(result);
                    allResult.addAll(hashResult);
                }
            }
            if (allResult.size() > 0) {
                typeAliasesPackage = String.join(",", (String[]) allResult.toArray(new String[0]));
            } else {
                throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误,参数typeAliasesPackage:" + typeAliasesPackage + "未找到任何包");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return typeAliasesPackage;
    }

    /**
     * @author huangjie
     * @date : 2022/03/17
     * @description
     */
    public static class CustomerIdGenerator implements IdentifierGenerator {
        @Override
        public Number nextId(Object entity) {
            return SnowFlakeUtil.getDefaultSnowFlakeId();
        }
    }
}