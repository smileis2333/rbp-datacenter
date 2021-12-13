package com.regent.rbp.api.dto.channel;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: rbp-datacenter
 * @description: 物理区域
 * @author: HaiFeng
 * @create: 2021-09-11 11:50
 */
@Data
@NoArgsConstructor
public class PhysicalRegion {

    public PhysicalRegion(String nation, String region, String province, String city, String county) {
        this.nation = nation;
        this.region = region;
        this.province = province;
        this.city = city;
        this.county = county;
    }

    private String nation;
    private String region;
    private String province;
    private String city;
    private String county;
}
