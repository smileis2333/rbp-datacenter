package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2022/01/19
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = {})
public @interface Code {
    String message() default "(${validatedValue})不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String targetTable();

    String targetField() default "code";
}
