package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购单 新增
 * @author: chenchungui
 * @create: 2021-12-21
 */
@Data
public class PurchaseBillSaveParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    @NotBlank
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date billDate;

    @ApiModelProperty(notes = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(notes = "供应商编号")
    @NotBlank
    private String supplierCode;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "供货类型")
    private String provideGoodsType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    @NotNull
    @BillStatus
    private Integer status;

    @Valid
    @NotEmpty
    @GoodsInfo
    @ApiModelProperty(notes = "货品明细")
    private List<PurchaseBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;
}
