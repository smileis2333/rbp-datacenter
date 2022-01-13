package com.regent.rbp.api.dto.sale;

import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * 营业员业绩
 * @author huangjie
 * @date : 2022/01/13
 * @description
 */
@FromTo(fromField = "employeeCode", toField = "employeeId")
@Data
public class EmployeeAchievement {
    @NotBlank
    private String employeeCode;

    private BigDecimal shareAmount;

    private BigDecimal shareRate;

    @Null
    private Long employeeId;
    @Null
    private Integer rowIndex;
}
