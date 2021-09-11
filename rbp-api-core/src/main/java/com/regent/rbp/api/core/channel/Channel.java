package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道档案
 * @author: HaiFeng
 * @create: 2021-09-11 15:42
 */
@Data
@ApiModel(description = "渠道档案")
@TableName(value = "rbp_channel")
public class Channel {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "渠道编号")
    private String code;

    @ApiModelProperty(notes = "渠道简称")
    private String name;

    @ApiModelProperty(notes = "渠道名称")
    private String fullName;

    @ApiModelProperty(notes = "建档日期")
    private Date buildDate;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省份")
    private String province;

    @ApiModelProperty(notes = "城市")
    private String city;

    @ApiModelProperty(notes = "区域")
    private String region;

    @ApiModelProperty(notes = "区/县")
    private String county;

    @ApiModelProperty(notes = "地址")
    private String address;

    @ApiModelProperty(notes = "行政区域1")
    private Long barrio1;

    @ApiModelProperty(notes = "行政区域2")
    private Long barrio2;

    @ApiModelProperty(notes = "行政区域3")
    private Long barrio3;

    @ApiModelProperty(notes = "行政区域4")
    private Long barrio4;

    @ApiModelProperty(notes = "行政区域5")
    private Long barrio5;

    @ApiModelProperty(notes = "组织架构1")
    private Long organization1;

    @ApiModelProperty(notes = "组织架构2")
    private Long organization2;

    @ApiModelProperty(notes = "组织架构3")
    private Long organization3;

    @ApiModelProperty(notes = "组织架构4")
    private Long organization4;

    @ApiModelProperty(notes = "组织架构5")
    private Long organization5;

    @ApiModelProperty(notes = "上级渠道")
    private Long parentChannelId;

    @ApiModelProperty(notes = "渠道业态")
    private Long businessFormatId;

    @ApiModelProperty(notes = "渠道等级")
    private Long gradeId;

    @ApiModelProperty(notes = "结算方式")
    private Long balanceTypeId;

    @ApiModelProperty(notes = "零售吊牌价类型")
    private Long retailTagPriceTypeId;

    @ApiModelProperty(notes = "分销吊牌价类型")
    private Long saleTagPriceTypeId;

    @ApiModelProperty(notes = "分公司")
    private Long branchCompanyId;

    @ApiModelProperty(notes = "销售范围")
    private Long saleRangeId;

    @ApiModelProperty(notes = "状态（0：未审核 1：已审核 2：反审核 3：作废）")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(value = "渠道经营性质")
    private Long businessNatureId;

    @ApiModelProperty(notes = "资金号")
    private Long fundAccountId;

    @ApiModelProperty(notes = "联系人")
    private String linkMan;

    @ApiModelProperty(notes = "联系人电话")
    private String linkManMobile;

    @ApiModelProperty(notes = "最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty(notes = "最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(notes = "渠道归属云仓")
    private Long wareHouseId;

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

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

}
