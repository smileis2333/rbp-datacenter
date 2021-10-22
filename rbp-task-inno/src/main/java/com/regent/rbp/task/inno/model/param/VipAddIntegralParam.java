package com.regent.rbp.task.inno.model.param;

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
    private Date VipAddIntegral_Date;
    @ApiModelProperty(notes = "手工单号，可存流水号以免重复充值", required = true)
    private String Manual_ID;
    @ApiModelProperty(notes = "VIP卡号", required = true)
    private String Vip;
    @ApiModelProperty(notes = "付款方式如没有填空")
    private String Payment;
    @ApiModelProperty(notes = "积分")
    private Integer Integral;
    @ApiModelProperty(notes = "是否允许负积分调整，1允许，0不允许")
    private Integer IsNegative;
    @ApiModelProperty(notes = "备注")
    private String Remark;
    @ApiModelProperty(notes = "优惠卷ID")
    private String TicketID;
    @ApiModelProperty(notes = "优惠卷类型")
    private Integer TicketTypeID;

}
