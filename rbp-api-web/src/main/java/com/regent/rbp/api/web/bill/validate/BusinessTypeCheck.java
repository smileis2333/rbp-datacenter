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
@Constraint(validatedBy = {BusinessTypeValidatorForSingle.class, BusinessTypeValidatorForQueryList.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BusinessTypeCheck {
    String message() default "业务类型参数输入非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}