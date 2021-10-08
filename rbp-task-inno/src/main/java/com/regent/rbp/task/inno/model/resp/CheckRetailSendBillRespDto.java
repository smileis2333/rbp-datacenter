package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.CheckRetailSendBillMainDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-24
 */
@Data
public class CheckRetailSendBillRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "结果")
    private CheckRetailSendBillMainDto data;

}
