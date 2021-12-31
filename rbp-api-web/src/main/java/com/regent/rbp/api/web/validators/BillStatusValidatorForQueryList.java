package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.validate.BillStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Iterator;
import java.util.List;

/**
 * 存在 side effect
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
public class BillStatusValidatorForQueryList implements ConstraintValidator<BillStatus, List<Integer>> {


    @Override
    public boolean isValid(List<Integer> statuses, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(statuses)) {
            return true;
        }
        Iterator<Integer> iterator = statuses.iterator();
        while (iterator.hasNext()) {
            Integer status = iterator.next();
            if (!(status >= 0 && status < 4)) {
                iterator.remove();
            }
        }

        if (CollUtil.isNotEmpty(statuses)) {
            return true;
        }
        return false;
    }
}
