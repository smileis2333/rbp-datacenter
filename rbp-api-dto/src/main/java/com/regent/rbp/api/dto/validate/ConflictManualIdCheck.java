package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检测 manualId 是否重复，用法类似
 * @see BillNo
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface ConflictManualIdCheck {
    String message() default "(${validatedValue})已经存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 检测手工单号的目标表
     *
     * @return
     */
    String targetTable();

}
