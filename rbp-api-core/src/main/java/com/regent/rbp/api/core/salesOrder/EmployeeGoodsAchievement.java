package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 营业员到货品业绩
 * @author huangjie
 * @date : 2022/01/13
 * @description
 */
@Data
@TableName(value = "rbp_employee_goods_achievement")
public class EmployeeGoodsAchievement {
    private Long id;
    private Long employeeId;
    private Long billId;
    private Integer billType;
    private Integer rowIndex;
    private Long goodsId;
    private Long colorId;
    private Long sizeId;
    private Long longId;
    private BigDecimal goodsAmount;
    private BigDecimal shareAmount;
    private BigDecimal shareRate;
    private BigDecimal quantity;
    private Date createdBy;
    private Date createdTime;
    private Date updatedBy;
    private Date updatedTime;
}
