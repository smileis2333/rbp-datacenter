package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: rbp-datacenter
 * @description: 收货信息
 * @author: HaiFeng
 * @create: 2021-09-11 13:22
 */
@Data
public class AddressData {

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long channelId;

    @ApiModelProperty("国家/地区")
    private String nation;

    @ApiModelProperty("州/省/地区")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("县/区")
    private String county;

    @ApiModelProperty("详细地址")
    @NotBlank
    private String address;

    @ApiModelProperty("联系人")
    @NotBlank
    private String contactsPerson;

    @ApiModelProperty("手机号码")
    @NotBlank
    private String mobile;

    @ApiModelProperty("邮政编码")
    private String postCode;

    @ApiModelProperty("默认标记(false-不标记，true-默认标记，默认为false)")
    private Boolean defaultFlag = false;
}
