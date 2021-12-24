package com.regent.rbp.api.web.bill.validate;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.BusinessType;
import com.regent.rbp.api.dao.salePlan.BusinessTypeDao;
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
public class BusinessTypeValidatorForQueryList implements ConstraintValidator<BusinessTypeCheck, List<String>> {
    @Autowired
    private BusinessTypeDao businessTypeDao;

    @Override
    public boolean isValid(List<String> businessTypes, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(businessTypes)){
            return true;
        }
        List<BusinessType> bts = businessTypeDao.selectList(new QueryWrapper<BusinessType>().in("name", businessTypes));
        if (CollUtil.isNotEmpty(bts)){
            return true;
        }
        return false;
    }
}
