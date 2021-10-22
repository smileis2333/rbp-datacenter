package com.regent.rbp.api.dto.storedvaluecard;

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
    private List<AddAmount> AmountList;
    @ApiModelProperty(notes = "生成时间", required = true)
    private Date VipAddValue_Date;
    @ApiModelProperty(notes = "手工单号")
    private String Manual_ID;
    @ApiModelProperty(notes = "VIP卡号", required = true)
    private String Vip;
    @ApiModelProperty(notes = "审核日期", required = true)
    private Date PostedDate;
    @ApiModelProperty(notes = "店铺编号")
    private String Customer_ID;
    @ApiModelProperty(notes = "营业员")
    private String BuisnessManID;
    @ApiModelProperty(notes = "核销单号")
    private String CheckOrderID;

}
