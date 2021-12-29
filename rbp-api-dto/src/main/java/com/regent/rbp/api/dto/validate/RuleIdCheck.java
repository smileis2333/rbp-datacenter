package com.regent.rbp.api.dto.validate;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RuleIdCheck {
    String message() default "条形码生成规则编号非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
