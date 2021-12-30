package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.CurrencyType;
import com.regent.rbp.api.dao.salePlan.CurrencyTypeDao;
import com.regent.rbp.api.dto.validate.CurrencyTypeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
@Component
public class CurrencyTypeCheckValidatorForSingle implements ConstraintValidator<CurrencyTypeCheck, String> {
    @Autowired
    private CurrencyTypeDao currencyTypeDao;

    @Override
    public boolean isValid(String currencyType, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(currencyType)) {
            return true;
        }
        if (CollUtil.isNotEmpty(currencyTypeDao.selectList(new QueryWrapper<CurrencyType>().eq("name", currencyType)))) {
            return true;
        }
        return false;
    }
}
