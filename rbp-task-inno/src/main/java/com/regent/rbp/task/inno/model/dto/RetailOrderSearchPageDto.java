package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderSearchPageDto {

    @ApiModelProperty(notes = "总数")
    private String totalPages;

    @ApiModelProperty(notes = "列表数据")
    private List<RetailOrderMainDto> data;

}
