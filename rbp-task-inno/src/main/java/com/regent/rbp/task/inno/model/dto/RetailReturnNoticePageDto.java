package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-26 16:17
 */
@Data
public class RetailReturnNoticePageDto {

    /**
     * 记录总页数
     */
    private Integer totalPages;

    private List<RetailReturnNoticeListDto> data;
}
