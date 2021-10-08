package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class UploadRetailSendBillDto {

    @ApiModelProperty(notes = "发货单号（ERP发货单主键）")
    private String RowUniqueKey;

    @ApiModelProperty(notes = "1：正常，其他：异常编码")
    private String code;

    @ApiModelProperty(notes = "提示")
    private String msg;

}
