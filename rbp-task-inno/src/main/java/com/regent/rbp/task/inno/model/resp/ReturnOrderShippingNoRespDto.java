package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 物流信息 请求返回
 * @author: HaiFeng
 * @create: 2022/4/19 16:43
 */
@Data
public class ReturnOrderShippingNoRespDto extends BaseResponseDto {

    @ApiModelProperty(notes = "请求返回")
    private List<InnoLogisticsDto> data;

}
