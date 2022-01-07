package com.regent.rbp.api.web.validators;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.validate.SupplierCodeCheck;
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
public class SupplierCodeValidatorForSingle implements ConstraintValidator<SupplierCodeCheck, String> {
    @Autowired
    private SupplierDao supplierDao;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            Supplier name = supplierDao.selectOne(new QueryWrapper<Supplier>().eq("code", value));
            return name != null;
        }
        return true;
    }
}
