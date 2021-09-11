package com.regent.rbp.api.core.fundAccount;

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
 * @description: 资金号
 * @author: HaiFeng
 * @create: 2021-09-11 15:15
 */
@Data
@ApiModel(description = "资金号")
@TableName(value = "rbp_fund_account")
public class FundAccount {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "上级资金号")
    private Long parentId;

    @ApiModelProperty(notes = "法人")
    private String legalPerson;

    @ApiModelProperty(notes = "信用额度")
    private BigDecimal credit;

    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(notes = "税号")
    private String taxNumber;

    @ApiModelProperty(notes = "状态（0：未审核 1：已审核 2：反审核 3：作废）")
    private Integer status;

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

    @ApiModelProperty(notes = "类型，0:全部，1:应收，2：应付")
    private Integer type;

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
