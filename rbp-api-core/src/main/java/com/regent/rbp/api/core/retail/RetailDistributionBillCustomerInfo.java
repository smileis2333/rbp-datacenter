package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
@ApiModel(description = "全渠道配单顾客信息 ")
@TableName(value = "rbp_retail_distribution_bill_customer_info")
public class RetailDistributionBillCustomerInfo {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "会员卡号")
    private Long memberCardId;

    @ApiModelProperty(notes = "会员卡号")
    @TableField(exist = false)
    private String memberCardCode;

    @ApiModelProperty(notes = "买家昵称")
    private String buyerNickname;

    @ApiModelProperty(notes = "买家账号")
    private String buyerAccount;

    @ApiModelProperty(notes = "买家邮箱")
    private String buyerEmail;

    @ApiModelProperty(notes = "物流公司")
    private Long logisticsCompanyId;

    @ApiModelProperty(notes = "物流公司名称")
    @TableField(exist = false)
    private String logisticsCompanyName;

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

    @ApiModelProperty(notes = "收货人")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机号码")
    private String mobile;

    @ApiModelProperty(notes = "邮编")
    private String postCode;

    @ApiModelProperty(notes = "物流费用")
    private BigDecimal logisticsAmount;

    @ApiModelProperty(notes = "说明")
    private String notes;

    @ApiModelProperty(notes = "导入说明")
    @TableField(exist = false)
    private String customerNotes;

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
}
