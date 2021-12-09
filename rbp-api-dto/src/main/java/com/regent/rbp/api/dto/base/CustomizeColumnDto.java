package com.regent.rbp.api.dto.base;

import lombok.Data;

import java.util.List;

@Data
public class CustomizeColumnDto {
    /**
     * 编码
     */
    private Long id;

    private String moduleId;

    private String groupKey;

    private String code;

    private String name;

    private String type;

    private List<CustomizeColumnValueDto> columnValueList;

}
