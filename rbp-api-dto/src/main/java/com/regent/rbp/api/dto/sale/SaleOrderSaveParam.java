package com.regent.rbp.api.dto.sale;

import com.regent.rbp.api.dto.base.CustomizeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单 新增/修改
 * @author: HaiFeng
 * @create: 2021-11-09 17:11
 */
@Data
public class SaleOrderSaveParam {

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "外部单号（RBP手工单号），唯一。")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private String billDate;

    @ApiModelProperty(notes = "销售渠道编号")
    private String saleChannelCode;

    @ApiModelProperty(notes = "渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "会员编号")
    private String memberCode;

    @ApiModelProperty(notes = "原单号")
    private String originBillNo;

    @ApiModelProperty(notes = "班次编号")
    private String shiftNo;

    @ApiModelProperty(notes = "来源(0.Pos;1.后台;2.第三方平台)")
    private Integer origin;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "单据类型(0.线下销售 1.全渠道发货 2.线上发货 3.线上退货 4.定金)")
    private Integer billType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "货品明细")
    private List<SalesOrderBillGoodsResult> goodsDetailData;

    @ApiModelProperty(notes = "支付方式")
    private List<SalesOrderBillPaymentResult> retailPayTypeData;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeData> customizeData;
}
