package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillUploadDto {

    @ApiModelProperty(notes = "全渠道发货单号")
    private String billNo;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "信息")
    private String message;

    public RetailSendBillUploadDto() {
    }

    public RetailSendBillUploadDto(String billNo, String code, String message) {
        this.billNo = billNo;
        this.code = code;
        this.message = message;
    }

}
