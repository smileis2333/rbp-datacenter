package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 更新退货单收货状态返回
 * @author: HaiFeng
 * @create: 2021-10-15 14:57
 */
@Data
public class UpdateReturnOrderStatusRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "请求返回")
    private List<InnoDataRespDto> data;
}
