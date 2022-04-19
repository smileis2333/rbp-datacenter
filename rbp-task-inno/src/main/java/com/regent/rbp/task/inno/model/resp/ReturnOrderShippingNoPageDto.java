package com.regent.rbp.task.inno.model.resp;

import com.regent.rbp.task.inno.model.dto.RetailOrderMainDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2022/4/19 19:32
 */
@Data
public class ReturnOrderShippingNoPageDto {

    @ApiModelProperty(notes = "总数")
    private Integer totalPages;

    @ApiModelProperty(notes = "列表数据")
    private List<InnoLogisticsDto> data;
}
