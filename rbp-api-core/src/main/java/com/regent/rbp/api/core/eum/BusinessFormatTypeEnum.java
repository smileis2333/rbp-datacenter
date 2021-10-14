package com.regent.rbp.api.core.eum;

/**
 * @program: rbp-datacenter
 * @description: 渠道业态 Enum
 * @author: HaiFeng
 * @create: 2021-10-13 15:09
 */
public enum BusinessFormatTypeEnum {

    // 仓库
    WAREHOUSE("001"),
    // 店铺
    STORE("002"),
    // 线上
    ONLINE("003");

    private String key;

    BusinessFormatTypeEnum(String key) { this.key = key; }

    public String getKey(){ return key;}
}
