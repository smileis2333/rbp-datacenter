package com.regent.rbp.api.dto.purchaseReturn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知 查询返回
 * @author: HaiFeng
 * @create: 2021/12/30 14:10
 */
@Data
public class PurchaseReturnNoticeBillQueryResult {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单据编号")
    private String billNo;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebula手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String toChannelCode;

    @ApiModelProperty(notes = "币种")
    private String currencyType;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核)")
    private Integer status;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty(notes = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    @ApiModelProperty(notes = "自定义字段")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<PurchaseReturnNoticeBillGoodsDetailData> goodsDetailData;
}
