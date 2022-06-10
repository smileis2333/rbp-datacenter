package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2022/02/15
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = {})
public @interface Dictionary {
    String message() default "{regent.validation.constraints.mapNotFound}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 检测手工单号的目标表
     *
     * @return
     */
    String targetTable();

    String targetField();

    /**
     * 单个校验才允许指定
     * @return
     */
    String operator() default Operator.EQUAL;
}

interface Operator {
    String EQUAL = "=";
}
