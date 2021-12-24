package com.regent.rbp.api.web.bill.validate;

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
@Constraint(validatedBy = ConflictManualIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConflictManualIdCheck {
    String message() default "手工单号重复";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 检测手工单号的目标表
     *
     * @return
     */
    Class value();

}
