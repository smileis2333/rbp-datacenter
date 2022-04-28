package com.regent.rbp.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.ConflictManualIdCheck;
import com.regent.rbp.api.dto.validate.DiscreteRange;
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
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class StockAdjustBillSaveParam {
    @ApiModelProperty(notes = "模块编号")
    @NotBlank
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    @NotBlank
    @ConflictManualIdCheck(targetTable = "rbp_stock_adjust_bill")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型名称")
    @NotBlank
    private String businessType;

    @ApiModelProperty(notes = "渠道编号")
    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核)")
    @NotNull
    @DiscreteRange(ranges = {0,1}, message = "单据状态(0.未审核,1.已审核")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "自定义字段")
    @Valid
    private List<CustomizeDataDto> customizeData;

    @NotEmpty
    @Valid
    @ApiModelProperty("货品明细")
    @GoodsInfo
    private List<StockAdjustBillGoodsDetailData> goodsDetailData;
}
