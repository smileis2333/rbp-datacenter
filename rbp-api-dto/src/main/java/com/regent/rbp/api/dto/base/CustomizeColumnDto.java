package com.regent.rbp.api.dto.base;

import lombok.Data;

@Data
public class CustomizeColumnDto {
    private String moduleId;
    private String code;
    private String name;
    private int type;
}
