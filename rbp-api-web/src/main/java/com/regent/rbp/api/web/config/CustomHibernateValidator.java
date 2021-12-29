package com.regent.rbp.api.web.config;

import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;

import javax.validation.ValidatorFactory;
import javax.validation.spi.ConfigurationState;
import java.lang.reflect.Field;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @description
 */
@Log4j2
public class CustomHibernateValidator extends HibernateValidator {

    @Override
    public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
        ValidatorFactoryImpl validatorFactory = new ValidatorFactoryImpl(configurationState);
        // 修改validatorFactory中原有的ConstraintHelper
        CustomConstraintHelper customConstraintHelper = new CustomConstraintHelper();
        try {
            Field field = validatorFactory.getClass().getDeclaredField("constraintHelper");
            field.setAccessible(true);
            field.set(validatorFactory,customConstraintHelper);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("{}",e);
        }
        // 我们自定义的CustomConstraintHelper，继承了原有的
        // org.hibernate.validator.internal.metadata.core.ConstraintHelper,这里对
        // 原有类中的注解--》注解处理器map进行修改，放进我们自定义的注解和注解处理器
        customConstraintHelper.modify();

        return validatorFactory;
    }
}