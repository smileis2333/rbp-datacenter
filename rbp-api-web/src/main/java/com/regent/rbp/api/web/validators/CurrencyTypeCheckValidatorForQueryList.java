package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.CurrencyType;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dto.validate.CurrencyTypeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
@Component
public class CurrencyTypeCheckValidatorForQueryList implements ConstraintValidator<CurrencyTypeCheck, List<String>> {
    @Autowired
    private CurrencyTypeDao currencyTypeDao;

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(value)) {
            return true;
        }
        if (CollUtil.isNotEmpty(currencyTypeDao.selectList(new QueryWrapper<CurrencyType>().in("name", value)))) {
            return true;
        }
        return false;
    }
}
