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
@Constraint(validatedBy = {ChannelCodeValidatorForSingle.class, ChannelCodeValidatorForQueryList.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChannelCodeCheck {
    String message() default "渠道编号非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
