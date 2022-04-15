package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-15
 * @Description
 */
@Data
public class RetailOrderStatusSearchPageDto {

    @ApiModelProperty(notes = "总数")
    private String totalPages;

    @ApiModelProperty(notes = "列表数据")
    private List<RetailOrderStatusDto> data;
}
