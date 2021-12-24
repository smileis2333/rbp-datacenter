package com.regent.rbp.api.web.bill.validate;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.BusinessType;
import com.regent.rbp.api.dao.salePlan.BusinessTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class BusinessTypeValidatorForSingle implements ConstraintValidator<BusinessTypeCheck, String> {
    @Autowired
    private BusinessTypeDao businessTypeDao;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            BusinessType name = businessTypeDao.selectOne(new QueryWrapper<BusinessType>().eq("name", value));
            return name != null;
        }
        return true;
    }
}
