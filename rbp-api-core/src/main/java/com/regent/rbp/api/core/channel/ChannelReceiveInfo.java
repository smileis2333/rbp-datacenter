package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道收货信息
 * @author: HaiFeng
 * @create: 2021-09-13 10:46
 */
@Data
@ApiModel(description = "渠道收货信息")
@TableName(value = "rbp_channel_receive_info")
public class ChannelReceiveInfo {

    public ChannelReceiveInfo(Long id, Long channelId, String nation, String province, String city, String county, String address, String contactsPerson, String mobile, String postCode, Boolean defaultFlag, Long createdBy, Date createdTime, Long updatedBy, Date updatedTime) {
        this.id = id;
        this.channelId = channelId;
        this.nation = nation;
        this.province = province;
        this.city = city;
        this.county = county;
        this.address = address;
        this.contactsPerson = contactsPerson;
        this.mobile = mobile;
        this.postCode = postCode;
        this.defaultFlag = defaultFlag;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
    }

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "渠道Id")
    private Long channelId;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省份")
    private String province;

    @ApiModelProperty(notes = "城市")
    private String city;

    @ApiModelProperty(notes = "区/县")
    private String county;

    @ApiModelProperty(notes = "详细地址")
    private String address;

    @ApiModelProperty(notes = "联系人")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机号码")
    private String mobile;

    @ApiModelProperty(notes = "邮政编码")
    private String postCode;

    @ApiModelProperty(notes = "默认标记")
    private Boolean defaultFlag;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}
