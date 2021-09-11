package com.regent.rbp.api.dto.channel;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 渠道品牌 Dto
 * @author: HaiFeng
 * @create: 2021-09-11 16:33
 */
@Data
public class ChannelBrandDto {

    private Long channelId;
    private Long brandId;
    private String brandCode;
    private String brandName;
}
