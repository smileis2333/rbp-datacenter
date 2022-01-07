package com.regent.rbp.api.service.bean.bill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

/**
 * @author huangjie
 * @date : 2022/01/07
 * @description
 */
public final class ValidateMessageUtil {
    private static Logger logger = LoggerFactory.getLogger(ValidateMessageUtil.class);

    public final static <T> boolean pass(Set<ConstraintViolation<T>> validateRes, List<String> messageList) {

        validateRes.forEach(e -> {
            String fromField = null;
            if ((fromField = (String) e.getConstraintDescriptor().getAttributes().get("fromField")) != null) {
                messageList.add(String.format("%s.%s: %s", e.getPropertyPath(), fromField, e.getMessage()));
            } else {
                messageList.add(String.format("%s: %s", e.getPropertyPath(), e.getMessage()));
            }
        });

        if (!messageList.isEmpty()) {
            return false;
        }
        return true;
    }

}
