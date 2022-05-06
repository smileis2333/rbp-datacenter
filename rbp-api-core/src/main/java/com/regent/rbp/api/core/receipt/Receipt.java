package com.regent.rbp.api.core.receipt;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description="收款单")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_receipt")
public class Receipt extends Model<Receipt> {
    @TableId("id")
    private Long id;
    private String moduleId;
    private String billNo;
    private String manualId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;
    private Long fundAccountId;
    private Long channelId;
    private Long fundAccountBankId;
    private String receiptAccount;
    private Long receiptTypeId;
    private Long currencyTypeId;
    private BigDecimal exchangeRate = new BigDecimal(0);
    private BigDecimal currencyAmount = new BigDecimal(0);
    private BigDecimal amount = new BigDecimal(0);
    private String notes;
    private Integer status;
    private Long checkBy;
    private Date checkTime;
    private Long cancelBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
    private Long uncheckBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uncheckTime;

}
