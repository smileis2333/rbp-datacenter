package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2022/04/06
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = {})
public @interface BusinessBill {
    String message() default "单据校验入口";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String baseModuleId();

    String baseTable();
}
