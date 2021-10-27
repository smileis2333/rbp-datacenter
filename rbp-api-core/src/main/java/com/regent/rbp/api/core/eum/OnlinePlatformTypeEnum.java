package com.regent.rbp.api.core.eum;

/**
 * @description: 电商平台类型 Enum
 * @author: chenchungui
 * @create: 2021-10-27
 */
public enum OnlinePlatformTypeEnum {

    // inno 电商
    INNO(1, "INNO"),

    ;

    private Integer id;

    private String key;

    OnlinePlatformTypeEnum(Integer id, String key) {
        this.id = id;
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public static String getKeyById(Integer id) {
        for (OnlinePlatformTypeEnum typeEnum : OnlinePlatformTypeEnum.values()) {
            if (typeEnum.getId().equals(id)) {
                return typeEnum.getKey();
            }
        }
        return null;
    }
}
