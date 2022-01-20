package com.regent.rbp.api.core.box;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
@TableName(value = "rbp_box")
public class Box {
    private Long id;

    private String code;

    private String manualId;

    private String batchNumber;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long channelId;

    private Long supplierId;

    private Long distributionTypeId;

    private Integer type;

    private Integer status;

    private Date buildDate;

    private Date effectiveDate;

    private Date expirationDate;

    private Integer validateDay;

    private String notes;

    private BigDecimal weight;

    private BigDecimal quantity;

    private String moduleId;

    private Long billId;

    @TableField(exist = false)
    List<BoxDetail> boxDetails = new ArrayList<>();
}
