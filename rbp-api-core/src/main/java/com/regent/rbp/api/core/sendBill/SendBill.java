package com.regent.rbp.api.core.sendBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 发货单
 * @author: HaiFeng
 * @create: 2021-11-16 17:17
 */
@Data
@ApiModel(description = "发货单")
@TableName(value = "rbp_send_bill")
public class SendBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "发货渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "收货渠道编号")
    private Long toChannelId;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;

    @ApiModelProperty(notes = "发货计划单号")
    private Long sendPlanId;

}
