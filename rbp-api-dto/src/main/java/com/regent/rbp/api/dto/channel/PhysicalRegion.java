package com.regent.rbp.api.dto.channel;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("国家")
    private String nation;

    @ApiModelProperty("区域")
    private String region;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区/县")
    private String county;
}
