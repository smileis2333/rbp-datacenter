package com.regent.rbp.api.web.validators;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill;
import com.regent.rbp.api.dao.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillDao;
import com.regent.rbp.api.dto.validate.PurchaseReceiveNoticeNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@Component
public class PurchaseReceiveNoticeNoValidator  implements ConstraintValidator<PurchaseReceiveNoticeNo, String> {
    @Autowired
    private PurchaseReceiveNoticeBillDao purchaseReceiveNoticeBillDao;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            return purchaseReceiveNoticeBillDao.selectOne(new QueryWrapper<PurchaseReceiveNoticeBill>().eq("bill_no", value)) != null;
        }
        return true;
    }
}
