package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: rbp-datacenter
 * @description: 收货信息
 * @author: HaiFeng
 * @create: 2021-09-11 13:22
 */
@Data
public class AddressData {

    @JsonIgnore
    private Long channelId;
    private String nation;
    private String province;
    private String city;
    private String county;
    @NotBlank
    private String address;
    @NotBlank
    private String contactsPerson;
    @NotBlank
    private String mobile;
    private String postCode;

    @NotNull
    private Boolean defaultFlag;
}
