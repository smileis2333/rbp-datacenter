package com.regent.rbp.api.core.onlinePlatform;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 电商平台档案 对象 rbp_online_platform
 *
 * @author xuxing
 */
@Data
@ApiModel(description = "电商平台档案 ")
@TableName(value = "rbp_online_platform")
public class OnlinePlatform {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "订阅方编号")
    private String code;

    @ApiModelProperty(notes = "订阅方名称")
    private String name;

    @ApiModelProperty(notes = "SessionKey")
    private String sessionKey;

    @ApiModelProperty(notes = "AppKey")
    private String appKey;

    @ApiModelProperty(notes = "AppSecret")
    private String appSecret;

    @ApiModelProperty(notes = "平台类型")
    private Integer onlinePlatformTypeId;

    @ApiModelProperty(notes = "平台类型名称")
    @TableField(exist = false)
    private String onlinePlatformTypeName;

    @ApiModelProperty(notes = "对应销售渠道")
    private Long channelId;

    @ApiModelProperty(notes = "对应销售渠道名称")
    @TableField(exist = false)
    private String channelName;

    @ApiModelProperty(notes = "对应销售渠道编码")
    @TableField(exist = false)
    private String channelCode;

    @ApiModelProperty(notes = "默认物流公司")
    private Long logisticsCompanyId;

    @ApiModelProperty(notes = "默认物流公司名称")
    @TableField(exist = false)
    private String logisticsCompanyName;

    @ApiModelProperty(notes = "外部系统")
    private Long externalApplicationId;

    @ApiModelProperty(notes = "外部系统名称")
    @TableField(exist = false)
    private String externalApplicationName;

    @ApiModelProperty(notes = "云仓")
    private Long warehouseId;

    @ApiModelProperty(notes = "云仓名称")
    @TableField(exist = false)
    private String warehouseName;

    @ApiModelProperty(notes = "云仓编号")
    @TableField(exist = false)
    private String warehouseCode;

    @ApiModelProperty(notes = "默认发货渠道")
    private Long sendChannelId;

    @ApiModelProperty(notes = "默认发货渠道名称")
    @TableField(exist = false)
    private String sendChannelName;

    @ApiModelProperty(notes = "外部系统API地址")
    private String externalApplicationApiUrl;

    @ApiModelProperty(notes = "发货人")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机号码")
    private String mobile;

    @ApiModelProperty(notes = "邮编")
    private String postCode;

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

    @ApiModelProperty(notes = "默认收退货渠道")
    private Long receiveChannelId;

    @ApiModelProperty(notes = "状态 (0：未审核 1：已审核 2：反审核 3：作废)")
    private Integer status;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建人名称")
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库默认时间
     */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    /**
     * 数据库默认时间
     */
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @ApiModelProperty(notes = "审核人名称")
    @TableField(exist = false)
    private String checkByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效人名称")
    @TableField(exist = false)
    private String cancelByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核人名称")
    @TableField(exist = false)
    private String uncheckByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;
}
