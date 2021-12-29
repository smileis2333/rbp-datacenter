package com.regent.rbp.api.web.validators;

import com.regent.rbp.api.dto.validate.FromTo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/29
 * todo pre validate field
 * @description
 */
@Component
@Log4j2
public class FromToValidator implements ConstraintValidator<FromTo, Object> {
    private String fromField;
    private String toField;

    @Override
    public void initialize(FromTo constraintAnnotation) {
        fromField = constraintAnnotation.fromField();
        toField = constraintAnnotation.toField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            final Object fromFieldVal = BeanUtils.getProperty(obj, fromField);
            final Object toFieldVal = BeanUtils.getProperty(obj, toField);

            return fromFieldVal == null || toFieldVal != null;
        } catch (final Exception ignore) {
            // ignore
            log.error("validate internal error");
        }
        return true;
    }
}
