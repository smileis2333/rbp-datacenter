package com.regent.rbp.api.dto.purchaseReturn;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 财股退货通知 新增
 * @author: HaiFeng
 * @create: 2021/12/31 10:34
 */
@Data
public class PurchaseReturnNoticeBillSaveParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebula手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private String billDate;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(notes = "退货渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "采购单号")
    private String purchaseNo;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核)")
    private Integer status;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<PurchaseReturnNoticeBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "货品自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

}
