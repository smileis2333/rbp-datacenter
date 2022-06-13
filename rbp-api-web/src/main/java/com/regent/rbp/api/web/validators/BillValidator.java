package com.regent.rbp.api.web.validators;

import com.regent.rbp.api.dto.purchase.AbstractBillSaveParam;
import com.regent.rbp.api.dto.validate.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2022/04/06
 * @description
 */
@Component
public class BillValidator implements ConstraintValidator<Bill, AbstractBillSaveParam> {
    private String baseModuleId;

    private String baseTable;

    @Autowired
    private CommonBillValidator commonBillValidator;


    @Override
    public void initialize(Bill constraintAnnotation) {
        baseModuleId = constraintAnnotation.baseModuleId();
        baseTable = constraintAnnotation.baseTable();
    }

    @Override
    public boolean isValid(AbstractBillSaveParam param, ConstraintValidatorContext constraintValidatorContext) {
        return commonBillValidator.validate(param, constraintValidatorContext, baseTable, baseModuleId);
    }

}
