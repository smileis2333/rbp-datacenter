package com.regent.rbp.api.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单查询返回
 * @author: HaiFeng
 * @create: 2021-11-08 14:16
 */
@Data
public class SalesOrderBillQueryResult {

    @ApiModelProperty(notes = "销售单Id")
    private Long id;

    @ApiModelProperty(notes = "外部单号（RBP手工单号），唯一。")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private String billDate;

    @ApiModelProperty(notes = "销售渠道编号")
    private String saleChannelCode;

    @ApiModelProperty(notes = "渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "会员编号")
    private String memberCode;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废)")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "来源（0-Pos，1-后台，2-第三方平台)")
    private Integer origin;

    @ApiModelProperty(notes = "原单号")
    private String originBillNo;

    @ApiModelProperty(notes = "单据类型（0-线下销售，1-全渠道发货，2-线上发货，3-线上退货，4-定金)")
    private Integer billType;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty(notes = "更新时间")
    private String updatedTime;

    @ApiModelProperty(notes = "审核时间")
    private String checkTime;

    @ApiModelProperty(notes = "货品明细")
    private List<SalesOrderBillGoodsResult> goodsDetailData;

    @ApiModelProperty(notes = "支付方式")
    private List<SalesOrderBillPaymentResult> retailPayTypeData;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;
}
