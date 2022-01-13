package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 营业员到单业绩
 *
 * @author huangjie
 * @date : 2022/01/13
 * @description
 */
@Data
@TableName(value = "rbp_employee_goods_achievement")
public class EmployeeBillAchievement {
    private Long id;
    private Long employeeId;
    private Long billId;
    private Integer billType;
    private BigDecimal billAmount;
    private BigDecimal shareAmount;
    private BigDecimal shareRate;
    private Date createdBy;
    private Date createdTime;
    private Date updatedBy;
    private Date updatedTime;

}
