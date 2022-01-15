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
public class DiscreteRangeValidator implements ConstraintValidator<DiscreteRange, Number> {
    private Set<Long> rangeSet = new HashSet<>();

    @Override
    public void initialize(DiscreteRange constraintAnnotation) {
        long[] ranges = constraintAnnotation.ranges();
        int rangesLength = ranges.length;
        for (long i : ranges) {
            rangeSet.add(i);
        }
        if (rangesLength != rangeSet.size()) {
            throw new RuntimeException("illegal config in @DiscreteRange");
        }
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value != null) {
            if (value instanceof Integer) {
                return rangeSet.contains((long) value.intValue());
            } else if (value instanceof Long) {
                return rangeSet.contains(value);
            }

        }
        return true;
    }
}
