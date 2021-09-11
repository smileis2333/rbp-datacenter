package com.regent.rbp.api.service.enums;

/**
 * @author xuxing
 */

public enum BaseModuleEnum {
    GOODS("120003"),
    ;

    private String baseModuleId;

    public String getBaseModuleId() {
        return baseModuleId;
    }

    BaseModuleEnum(String baseModuleId) {
        this.baseModuleId = baseModuleId;
    }
}
