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
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
public @interface Code {
    String message() default "{regent.validation.constraints.mapNotFound}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String targetTable();

    String mapField() default "code";
}
