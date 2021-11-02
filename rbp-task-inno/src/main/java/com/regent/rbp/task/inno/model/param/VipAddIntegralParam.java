package com.regent.rbp.task.inno.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/20 13:54
 */
@Data
public class VipAddIntegralParam {
    @ApiModelProperty(notes = "调整时间", required = true)
    @JsonProperty("VipAddIntegral_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipAddIntegralDate;
    @ApiModelProperty(notes = "手工单号，可存流水号以免重复充值", required = true)
    @JsonProperty("Manual_ID")
    private String manualId;
    @ApiModelProperty(notes = "VIP卡号", required = true)
    @JsonProperty("VIP")
    private String vip;
    @ApiModelProperty(notes = "付款方式如没有填空")
    @JsonProperty("Payment")
    private String payment;
    @ApiModelProperty(notes = "积分")
    @JsonProperty("Integral")
    private Integer integral;
    @ApiModelProperty(notes = "是否允许负积分调整，1允许，0不允许")
    @JsonProperty("IsNegative")
    private Integer isNegative;
    @ApiModelProperty(notes = "备注")
    @JsonProperty("Remark")
    private String remark;
    @ApiModelProperty(notes = "优惠卷ID")
    @JsonProperty("TicketID")
    private String ticketId;
    @ApiModelProperty(notes = "优惠卷类型")
    @JsonProperty("TicketTypeID")
    private Integer ticketTypeId;

}
