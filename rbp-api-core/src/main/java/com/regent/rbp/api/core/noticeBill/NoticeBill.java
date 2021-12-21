package com.regent.rbp.api.core.noticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 指令单
 * @author: HaiFeng
 * @create: 2021-11-16 15:47
 */
@Data
@ApiModel(description = "指令单主表")
@TableName(value = "rbp_notice_bill")
public class NoticeBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "发货渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "收货渠道编号")
    private Long toChannelId;

    @ApiModelProperty(notes = "价格类型")
    private Long priceTypeId;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;

    @ApiModelProperty(notes = "计划单号")
    private Long salePlanId;

    @ApiModelProperty(notes = "科目编号")
    private Integer subjectId;

}
