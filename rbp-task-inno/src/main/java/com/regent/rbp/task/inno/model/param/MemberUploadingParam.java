package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 上传请求
 * @author: HaiFeng
 * @create: 2021-09-24 15:47
 */
@Data
public class MemberUploadingParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;
}
