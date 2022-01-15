package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 等于于
 * @see com.regent.rbp.api.dto.validate.DiscreteRange
 * 只是用于用于使用上方便
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface BillStatus {
    String message() default "{regent.validation.constraints.billStatus}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
