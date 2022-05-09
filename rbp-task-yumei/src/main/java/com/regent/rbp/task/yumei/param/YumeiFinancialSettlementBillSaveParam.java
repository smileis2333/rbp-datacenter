package com.regent.rbp.task.yumei.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@Data
public class YumeiFinancialSettlementBillSaveParam {

    @NotEmpty
    @ApiModelProperty(notes = "外部单号，唯一")
    private String manualId;

    @NotNull
    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotEmpty
    @ApiModelProperty(notes = "资金号")
    private String fundAccount;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @NotNull
    @ApiModelProperty(notes = "单据状态;1.已审核")
    private Integer status;

    @NotEmpty
    @ApiModelProperty(notes = "线上单号")
    private String onlineOrderCode;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @Valid
    @NotEmpty
    @GoodsInfo
    @ApiModelProperty(notes = "货品明细")
    private List<YumeiFinancialSettlementBillGoodsParam> goodsDetailData;

}
