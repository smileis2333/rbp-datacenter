package com.regent.rbp.api.dto.channel;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 物理区域
 * @author: HaiFeng
 * @create: 2021-09-11 11:50
 */
@Data
public class PhysicalRegion {

    private String nation;
    private String region;
    private String province;
    private String city;
    private String county;
}
