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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface ConflictManualIdCheck {
    String message() default "手工单号重复";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 检测手工单号的目标表
     *
     * @return
     */
    String targetClass();

}
