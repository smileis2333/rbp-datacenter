package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill;
import com.regent.rbp.api.dao.purchaseBill.PurchaseBillDao;
import com.regent.rbp.api.dao.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBillDao;
import com.regent.rbp.api.dto.validate.BillNo;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class BillNoValidator implements ConstraintValidator<BillNo, String> {
    private Class clazz;

    // support billBo of specific bill, for secondary cache support
    @Autowired
    private PurchaseBillDao purchaseBillDao;
    @Autowired
    private PurchaseReceiveNoticeBillDao purchaseReceiveNoticeBillDao;


    public static Map<String, Class> supportTables = null;

    static {
        supportTables = new HashMap<>();
        supportTables.put(TableConstants.PURCHASE_BILL, PurchaseBill.class);
        supportTables.put(TableConstants.PURCHASE_RECEIVE_NOTICE_BILL, PurchaseReceiveNoticeBill.class);
    }

    @Override
    public void initialize(BillNo constraintAnnotation) {
        if (StrUtil.isBlank(constraintAnnotation.targetTable())){
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
        if (supportTables.containsKey(constraintAnnotation.targetTable())) {
            clazz = supportTables.get(constraintAnnotation.targetTable());
        } else {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean isValid(String billNo, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(billNo)) {
            if (clazz == PurchaseBill.class) {
                return purchaseBillDao.selectList(new QueryWrapper<PurchaseBill>().eq("bill_no", billNo)) != null;
            } else if (clazz == PurchaseReceiveNoticeBill.class) {
                return purchaseReceiveNoticeBillDao.selectList(new QueryWrapper<PurchaseReceiveNoticeBill>().eq("bill_no", billNo)) != null;
            }
        }
        return true;
    }
}
