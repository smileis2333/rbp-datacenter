package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "供应商档案 ")
@TableName(value = "rbp_supplier")
public class Supplier {
    @TableId("id")
    private Long id;

    private String code;

    private String name;

    private String fullName;

    private Integer type;

    private Integer receiveDifferentType;

    private BigDecimal receiveDifferentPercent;

    private Long natureId;

    private Long gradeId;

    private String headPerson;

    private String tel1;

    private String tel2;

    private String address;

    private Long fundAccountId;

    private BigDecimal taxrate;

    private String notes;

    private Date createdTime;

    private Date updatedTime;

    private Date checkTime;

    private Long nation;

    private Long province;

    private Long city;

    private Long county;

}
