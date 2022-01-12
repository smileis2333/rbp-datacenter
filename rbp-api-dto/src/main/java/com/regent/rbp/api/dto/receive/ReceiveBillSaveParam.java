package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
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
    @NotBlank
    private String moduleId;

    @NotBlank
    @ConflictManualIdCheck(targetTable = "com.regent.rbp.api.core.receiveBill.ReceiveBill")
    private String manualId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotNull
    @BusinessTypeCheck
    private String businessType;

    @NotBlank
    @SupplierCodeCheck
    private String channelCode;

    @NotBlank
    @SupplierCodeCheck
    private String toChannelCode;

    @CurrencyTypeCheck
    private String currencyType;

    private String notes;

    @BillNo(targetTable = "rbp_send_bill")
    private String sendNo;

    @BillNo(targetTable = "rbp_notice_bill")
    private String noticeNo;

    @NotNull
    @BillStatus
    private Integer status;
    private List<CustomizeDataDto> customizeData;

    @Valid
    @NotEmpty
    private List<ReceiveBillGoodsDetailData> goodsDetailData;
    private Long baseBusinessTypeId;

    public String getNoticeNo() {
        if (baseBusinessTypeId != null && baseBusinessTypeId == 1100000000000023l) {
            return noticeNo;
        }
        return null;
    }
}
