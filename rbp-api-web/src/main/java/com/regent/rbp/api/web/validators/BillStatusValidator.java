package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.validate.BillStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
public class BillStatusValidator implements ConstraintValidator<BillStatus, Integer> {
    private Set<Integer> statusSet = CollUtil.newHashSet(0, 1, 2, 3);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value != null) {
            return statusSet.contains(value);
        }
        return true;
    }
}
