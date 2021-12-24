package com.regent.rbp.api.web.bill.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
public class BillStatusValidator implements ConstraintValidator<BillStatus, Integer> {


    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value != null) {
            return value >= 0 && value < 4;
        }
        return true;
    }
}
