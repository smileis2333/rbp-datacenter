package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * todo more accurate match via moduleId
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface BusinessTypeCheck {
    String message() default "(${validatedValue})不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}