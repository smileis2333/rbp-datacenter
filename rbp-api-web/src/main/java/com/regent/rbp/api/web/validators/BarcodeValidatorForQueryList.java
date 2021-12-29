package com.regent.rbp.api.web.validators;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dto.validate.BarcodeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @description
 */
@Component
public class BarcodeValidatorForQueryList implements ConstraintValidator<BarcodeCheck, List<String>> {
    @Autowired
    private BarcodeDao barcodeDao;

    @Override
    public boolean isValid(List<String> barcodes, ConstraintValidatorContext context) {
        if (barcodes.isEmpty()) {
            return true;
        }
        return !barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).isEmpty();
    }
}
