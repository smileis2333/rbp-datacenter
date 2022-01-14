package com.regent.rbp.api.web.validators;

import com.regent.rbp.api.dto.validate.DiscreteRange;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author huangjie
 * @date : 2022/01/14
 * @description
 */
@Component
public class DiscreteRangeValidator implements ConstraintValidator<DiscreteRange, Integer> {
    private Set<Integer> rangeSet = new HashSet<>();

    @Override
    public void initialize(DiscreteRange constraintAnnotation) {
        int[] ranges = constraintAnnotation.ranges();
        int rangesLength = ranges.length;
        for (int i : ranges) {
            rangeSet.add(i);
        }
        if (rangesLength != rangeSet.size()) {
            throw new RuntimeException("illegal config in @DiscreteRange");
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value != null) {
            return rangeSet.contains(value);
        }
        return true;
    }
}
