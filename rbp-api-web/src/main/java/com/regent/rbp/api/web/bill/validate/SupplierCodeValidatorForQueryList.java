package com.regent.rbp.api.web.bill.validate;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class SupplierCodeValidatorForQueryList implements ConstraintValidator<SupplierCodeCheck, List<String>> {
    @Autowired
    private SupplierDao supplierDao;

    @Override
    public boolean isValid(List<String> codes, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(codes)){
            return true;
        }
        List<Supplier> scs = supplierDao.selectList(new QueryWrapper<Supplier>().in("code", codes));
        if (CollUtil.isNotEmpty(scs)){
            return true;
        }
        return false;
    }
}
