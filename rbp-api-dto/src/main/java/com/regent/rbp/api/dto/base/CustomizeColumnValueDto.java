package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author chenchungui
 * @date 2021/12/9
 * @description
 */
@Data
public class CustomizeColumnValueDto {

    /**
     * 编码
     */
    private Long id;
    /**
     * 自定义字段编码
     */
    private Long customizeColumnId;
    /**
     * 字段值
     */
    private String value;
    /**
     * 序号
     */
    private Integer orderNumber;
    /**
     * 默认标记(0-非默认值 1-默认值)
     */
    private Boolean defaultFlag;

}
