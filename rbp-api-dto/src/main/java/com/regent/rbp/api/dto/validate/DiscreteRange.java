package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 离散范围校验
 * 系统目前大量存在数值入参限制，
 * 从理论上来讲，这些数据不存在连续性的概念，只是当前系统的数值范围暂时还是连续
 * 因此，定义该注解用于离散数值上的值域校验
 * 使用者根据实际情况进行信息填写，如果重复性高，建议定义一个新注解减少代码重复，如
 * @see BillStatus
 * todo refactor to support all other primitive type
 * @author huangjie
 * @date : 2022/01/14
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
public @interface DiscreteRange {
    String message() default "参数范围不匹配";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] ranges() default {};
}
