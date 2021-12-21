package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购单 查询返回
 * @author: chenchungui
 * @create: 2021-12-21
 */
@Data
public class PurchaseBillQueryResult {

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "供货类型")
    private String provideGoodsType;

    @ApiModelProperty(notes = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(notes = "供应商编号")
    private String supplierCode;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
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
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<PurchaseBillGoodsDetailData> goodsDetailData;

}
