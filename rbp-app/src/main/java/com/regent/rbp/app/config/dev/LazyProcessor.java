package com.regent.rbp.app.config.dev;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_APPLICATION;

/**
 * @author huangjie
 * @date : 2022/04/12
 * @description
 */
@Profile("speedDev")
@Component
public class LazyProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Iterator<String> beanNames = beanFactory.getBeanNamesIterator();
        beanNames.forEachRemaining(beanName -> {
            try {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                int role = beanDefinition.getRole();
                if (role == ROLE_APPLICATION) {
                    beanDefinition.setLazyInit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        beanFactory.getBeanDefinition("pluginBeanConfig").setLazyInit(false);
        beanFactory.getBeanDefinition("applicationContextUtil").setLazyInit(false);
    }
}
