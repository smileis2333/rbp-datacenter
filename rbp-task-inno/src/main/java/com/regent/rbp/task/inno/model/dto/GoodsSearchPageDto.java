package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsSearchPageDto {
    private String totalPages;
    private List<GoodsItemDto> data;
}
