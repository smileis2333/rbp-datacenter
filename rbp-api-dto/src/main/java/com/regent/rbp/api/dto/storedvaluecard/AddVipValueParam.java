package com.regent.rbp.api.dto.storedvaluecard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 10:55
 */
@Data
public class AddVipValueParam {
    @ApiModelProperty(notes = "付款类型", required = true)
    @JsonProperty("AmountList")
    private List<AddAmount> amountList;
    @ApiModelProperty(notes = "生成时间", required = true)
    @JsonProperty("VipAddValue_Date")
    private Date vipAddValueDate;
    @ApiModelProperty(notes = "手工单号")
    @JsonProperty("Manual_ID")
    private String manualId;
    @ApiModelProperty(notes = "VIP卡号", required = true)
    @JsonProperty("Vip")
    private String vip;
    @ApiModelProperty(notes = "审核日期", required = true)
    @JsonProperty("PostedDate")
    private Date postedDate;
    @ApiModelProperty(notes = "店铺编号")
    @JsonProperty("Customer_ID")
    private String customerId;
    @ApiModelProperty(notes = "营业员")
    @JsonProperty("BuisnessManID")
    private String businessManId;
    @ApiModelProperty(notes = "核销单号")
    @JsonProperty("CheckOrderID")
    private String checkOrderId;

}
