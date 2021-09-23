package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 员工档案 上传请求
 */
@Data
public class EmployeeUploadingParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;

}
