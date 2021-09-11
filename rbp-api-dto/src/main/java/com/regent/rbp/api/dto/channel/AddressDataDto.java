package com.regent.rbp.api.dto.channel;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 收货信息
 * @author: HaiFeng
 * @create: 2021-09-11 13:22
 */
@Data
public class AddressDataDto {

    private String nation;
    private String province;
    private String city;
    private String county;
    private String address;
    private String contactsPerson;
    private String mobile;
    private String postCode;
}
