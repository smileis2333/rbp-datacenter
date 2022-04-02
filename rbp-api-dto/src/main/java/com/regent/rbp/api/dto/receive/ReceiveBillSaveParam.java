package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
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
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillSaveParam {
    @ApiModelProperty(notes = "模块编号")
    @NotBlank
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    @NotBlank
    @ConflictManualIdCheck(targetTable = "rbp_receive_bill")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型名称")
    @NotNull
    @BusinessTypeCheck
    private String businessType;

    @ApiModelProperty(notes = "发货渠道编号")
    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    @NotBlank
    @ChannelCodeCheck
    private String toChannelCode;

    @ApiModelProperty(notes = "币种名称")
    @CurrencyType
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty("发货单单号")
    @BillNo(targetTable = "rbp_send_bill")
    private String sendNo;

    @ApiModelProperty("指令单单号")
    @BillNo(targetTable = "rbp_notice_bill")
    private String noticeNo;

    @ApiModelProperty("单据状态(0.未审核,1.已审核,2.反审核)")
    @NotNull
    @DiscreteRange(ranges = {0, 1, 2}, message = "单据状态(0.未审核,1.已审核,2.反审核)")
    private Integer status;

    @ApiModelProperty("自定义字段")
    @Valid
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty("货品明细")
    @Valid
    @NotEmpty
    @GoodsInfo
    private List<ReceiveBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(hidden = true)
    private Long baseBusinessTypeId;

    public String getNoticeNo() {
        if (baseBusinessTypeId != null && baseBusinessTypeId == 1100000000000023l) {
            return noticeNo;
        }
        return null;
    }
}
