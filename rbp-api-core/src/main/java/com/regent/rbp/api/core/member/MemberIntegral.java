package com.regent.rbp.api.core.member;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 会员积分
 * @author: HaiFeng
 * @create: 2021-09-24 14:49
 */
@Data
@ApiModel(description = "会员积分")
@TableName(value = "rbp_member_integral")
public class MemberIntegral {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "会员卡id")
    private Long memberCardId;

    @ApiModelProperty(notes = "会员卡号")
    private String cardNo;

    @ApiModelProperty(notes = "积分")
    private Integer integral;

    @ApiModelProperty(notes = "冻结积分")
    private Integer frozenIntegral;

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
