package com.regent.rbp.api.core.receiveBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel(description="库存结存单 ")
@TableName(value = "rbp_stock_balance_bill")
public class StockBalanceBill{
    @TableId("id")
    private Long id;

    private Long createdBy;
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private Long updatedBy;

    @TableField(exist = false)
    private String updatedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private String moduleId;

    private String billNo;

    private Boolean status;

    private Date billDate;

    private String manualId;

    private Long channelId;

    private String notes;

    private BigDecimal totalQuantity;

}
