package com.regent.rbp.api.dto.storedvaluecard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 10:59
 */
@Data
public class AddAmount {
    @ApiModelProperty(notes = "付款编号", required = true)
    @JsonProperty("PaymentID")
    private String PaymentID;
    @ApiModelProperty(notes = "充值金额", required = true)
    @JsonProperty("FactAmount")
    private BigDecimal FactAmount;
    @ApiModelProperty(notes = "提增金额", required = true)
    @JsonProperty("Amount")
    private BigDecimal Amount;
    @ApiModelProperty(notes = "积分", required = true)
    @JsonProperty("Integral")
    private BigDecimal Integral;
}
