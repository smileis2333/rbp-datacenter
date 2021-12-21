package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 销售计划
 * @author: HaiFeng
 * @create: 2021-11-15 15:02
 */
@Data
@TableName(value = "rbp_sale_plan_bill")
public class SalePlanBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "价格类型")
    private Long priceTypeId;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;

    @ApiModelProperty(notes = "科目编号")
    private Integer subjectId;


}
