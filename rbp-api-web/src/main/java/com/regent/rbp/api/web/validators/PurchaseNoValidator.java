package com.regent.rbp.api.web.validators;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dto.validate.PurchaseNo;
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
public class PurchaseNoValidator implements ConstraintValidator<PurchaseNo, String> {
    @Autowired
    private PurchaseBillDao purchaseBillDao;

    @Override
    public boolean isValid(String purchaseNo, ConstraintValidatorContext context) {
        if (purchaseNo != null) {
            return purchaseBillDao.selectOne(new QueryWrapper<PurchaseBill>().eq("bill_no", purchaseNo)) != null;
        }
        return true;
    }
}
