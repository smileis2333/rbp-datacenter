package com.regent.rbp.api.service.sale.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单查询上下文对象
 * @author: HaiFeng
 * @create: 2021-11-08 14:48
 */
@Data
public class SalesOrderBillQueryContext {

    @ApiModelProperty(notes = "外部单号（RBP手工单号），唯一。")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "销售渠道编号")
    private long[] saleChannelCode;

    @ApiModelProperty(notes = "渠道编号")
    private long[] channelCode;

    @ApiModelProperty(notes = "会员编号")
    private long[] memberCode;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废)")
    private Integer[] status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "来源（0-Pos，1-后台，2-第三方平台)")
    private Integer[] origin;

    @ApiModelProperty(notes = "原单号")
    private String originBillNo;

    @ApiModelProperty(notes = "单据类型（0-线下销售，1-全渠道发货，2-线上发货，3-线上退货，4-定金)")
    private Integer[] billType;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    private Date createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    private Date createdDateEnd;

    @ApiModelProperty(notes = "审核日期(开始日期)")
    private Date checkDateStart;

    @ApiModelProperty(notes = "审核日期(截止日期)")
    private Date checkDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    private Date updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    private Date updatedDateEnd;

    @ApiModelProperty(notes = "需返回的字段列表")
    private String fields;

    @ApiModelProperty(notes = "页码：默认1")
    private Integer pageNo;

    @ApiModelProperty(notes = "每页条数：默认100条")
    private Integer pageSize;
}
