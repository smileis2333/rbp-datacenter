package com.regent.rbp.api.web.config;

import com.regent.rbp.api.dto.validate.ColorCode;
import com.regent.rbp.api.dto.validate.FromTo;
import com.regent.rbp.api.web.validators.ColorCodeValidator;
import com.regent.rbp.api.dto.validate.RuleIdCheck;
import com.regent.rbp.api.web.validators.RuleIdCheckValidator;
import com.regent.rbp.api.web.validators.FromToValidator;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorDescriptor;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

import javax.validation.ConstraintValidator;
import javax.validation.spi.ValidationProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @see ValidationProvider
 * for unreasonable/excess package arrangement(split the transport object from context),
 * so we need to split validate annotations and concrete validator,
 * the implment of this class is copy all config item from hibenrate default value,
 * and then inject regent custom validator(using reflect), and write back.
 * the key point is the jsr303 SPI.
 *
 * META-INF/services/javax.validation.spi.ValidationProvider
 * @description
 */
@Log4j2
public class CustomConstraintHelper extends ConstraintHelper {

    public CustomConstraintHelper() {
        super();
    }

    void modify() {
        Field field = null;
        try {
            field = this.getClass().getSuperclass().getDeclaredField("builtinConstraints");
            field.setAccessible(true);

            Object o = field.get(this);

            // 因为field被定义为了private final，且实际类型为
            // this.builtinConstraints = Collections.unmodifiableMap( tmpConstraints );
            // 因为不能修改，所以我这里只能拷贝到一个新的hashmap，再反射设置回去
            Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>> tmpConstraints = new HashMap<>();
            tmpConstraints.putAll((Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>>) o);
            // 在这里注册我们自定义的注解和注解处理器
            putConstraint(tmpConstraints, ColorCode.class, ColorCodeValidator.class);
            putConstraint(tmpConstraints, RuleIdCheck.class, RuleIdCheckValidator.class);
            putConstraint(tmpConstraints, FromTo.class, FromToValidator.class);
            /**
             * 设置回field
             */
            field.set(this, tmpConstraints);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("{}", e);
        }

    }

    private static <A extends Annotation> void putConstraint(Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>> validators,
                                                             Class<A> constraintType, Class<? extends ConstraintValidator<A, ?>> validatorType) {
        validators.put(constraintType, Collections.singletonList(ConstraintValidatorDescriptor.forClass(validatorType, constraintType)));
    }
}