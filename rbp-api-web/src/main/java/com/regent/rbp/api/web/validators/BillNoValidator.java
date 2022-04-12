package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.BillNo;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
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
public class BillNoValidator implements ConstraintValidator<BillNo, String> {

    protected String tableName;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(BillNo constraintAnnotation) {
        if (StrUtil.isBlank(constraintAnnotation.targetTable())){
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
        this.tableName = constraintAnnotation.targetTable();
    }

    @Override
    public boolean isValid(String billNo, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(billNo) && StrUtil.isNotEmpty(tableName)) {
            String existRow = baseDbDao.getStringDataBySql(String.format("select bill_no from %s where bill_no = '%s' limit 1", tableName, billNo));
            return StrUtil.isNotBlank(existRow);
        }
        return true;
    }
}
