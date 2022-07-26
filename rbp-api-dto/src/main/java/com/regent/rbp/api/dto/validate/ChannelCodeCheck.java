package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 * todo 复用@Code，跳掉重复的校验器实现
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface ChannelCodeCheck {
    String message() default "(${validatedValue})不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
