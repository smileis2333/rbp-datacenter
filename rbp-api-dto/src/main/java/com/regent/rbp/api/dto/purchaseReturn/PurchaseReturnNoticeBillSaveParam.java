package com.regent.rbp.api.dto.purchaseReturn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 财股退货通知 新增
 * @author: HaiFeng
 * @create: 2021/12/31 10:34
 */
@Data
public class PurchaseReturnNoticeBillSaveParam {

    @NotBlank
    private String moduleId;

    @NotBlank
    private String manualId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotBlank
    @BusinessTypeCheck
    private String businessType;

    @NotBlank
    @SupplierCodeCheck
    private String supplierCode;

    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    @BillNo(targetTable = "rbp_purchase_bill")
    private String purchaseNo;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @NotNull
    @BillStatus
    private Integer status;

    @ApiModelProperty(notes = "自定义字段")
    @Valid
    private List<CustomizeDataDto> customizeData;

    @NotEmpty
    @Valid
    private List<PurchaseReturnNoticeBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "货品自定义字段")
    @Valid
    private List<CustomizeDataDto> goodsCustomizeData;

}
