package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.param.UploadRetailSendBillParam;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class UploadRetailSendBillReqDto extends BaseRequestDto {

    private List<UploadRetailSendBillParam> data;

}
