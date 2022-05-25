package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.validate.SupplierCodeCheck;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class SupplierCodeValidatorForSingle implements ConstraintValidator<SupplierCodeCheck, String> {
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private DbDao dbDao;

    private boolean requireFundAccount;

    @Override
    public void initialize(SupplierCodeCheck constraintAnnotation) {
        requireFundAccount = constraintAnnotation.requireFundAccount();
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isBlank(code)) {
            return true;
        }
        HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        context.disableDefaultConstraintViolation();
        Supplier supplier = supplierDao.selectOne(Wrappers.lambdaQuery(Supplier.class).eq(Supplier::getCode, code));
        if (supplier==null){
            context.addExpressionVariable("validatedValue", code);
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addConstraintViolation();
            return false;
        }
        if (supplier.getStatus()!= CheckEnum.CHECK.getStatus()){
            context.addExpressionVariable("validatedValue", code);
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.noCheck}").addConstraintViolation();
            return false;
        }
        if (requireFundAccount){
            StringBuilder sql = new StringBuilder();
            sql.append("select a.id, a.name from rbp_fund_account a where a.status = 1 ");
            sql.append(String.format("and exists(select 1 from rbp_supplier b where b.id = %s and b.status = 1 and b.fund_account_id = a.id)", supplier.getId()));
            Map<String, Object> fundAccountMap = this.dbDao.selectMapById(sql.toString());
            if (CollUtil.isEmpty(fundAccountMap)) {
                context.addExpressionVariable("validatedValue", code);
                context.buildConstraintViolationWithTemplate("供应商需要设置资金号").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
