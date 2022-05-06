package com.regent.rbp.api.dto.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.Code;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.group.Standard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class ReceiptBillSaveParam {
    @NotBlank
    private String moduleId;

    @NotBlank
    private String manualId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ChannelCodeCheck
    private String channelCode;

    @NotBlank
    @Code(targetTable = "rbp_fund_account")
    private String fundAccountCode;

    private String fundAccountBank;

    private String receiptAccount;

    @NotBlank
    private String receiptType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @NotNull(groups = Standard.class)
    private BigDecimal currencyAmount;

    @NotNull
    private BigDecimal amount;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @NotNull
    @DiscreteRange(ranges = {0, 1}, message = "单据状态(0.未审核,1.已审核)")
    private Integer status;


    @ApiModelProperty(notes = "自定义字段")
    @Valid
    private List<CustomizeDataDto> customizeData;

}
