package com.regent.rbp.api.core.storedvaluecard;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 储值卡
 * @author: HaiFeng
 * @create: 2021-11-03 19:32
 */
@Data
@ApiModel(description = "储值卡")
@TableName(value = "rbp_stored_value_card")
public class StoredValueCard {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "卡号")
    private String cardNo;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "顾客姓名")
    private String name;

    @ApiModelProperty(notes = "顾客手机")
    private String phone;

    @ApiModelProperty(notes = "类型 1-不记名卡2-会员卡")
    private Integer type;

    @ApiModelProperty(notes = "状态 0-未审核 1-审核")
    private Integer status;

    @ApiModelProperty(notes = "开卡日期")
    private Date openCardDate;

    @ApiModelProperty(notes = "生效日期")
    private Date beginDate;

    @ApiModelProperty(notes = "失效日期")
    private Date endDate;

    @ApiModelProperty(notes = "储值卡政策编码")
    private Long storedValueCardPolicyId;

    @ApiModelProperty(notes = "会员编码")
    private Long memberCardId;

    @ApiModelProperty(notes = "IC卡")
    private String icCardNo;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
