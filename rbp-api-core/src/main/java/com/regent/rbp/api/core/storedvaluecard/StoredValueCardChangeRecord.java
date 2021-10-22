package com.regent.rbp.api.core.storedvaluecard;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 17:01
 */
@Data
@ApiModel(description = "储值卡变动记录")
@TableName(value = "rbp_stored_value_card_change_record")
public class StoredValueCardChangeRecord extends Model<StoredValueCardChangeRecord> {
    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "储值卡编码")
    private Long storedValueCardId;

    @ApiModelProperty(notes = "会员卡号")
    @TableField(exist = false)
    private String memberCardNo;
    @ApiModelProperty(notes = "会员姓名")
    @TableField(exist = false)
    private String memberCardName;
    @ApiModelProperty(notes = "变动类型 1-消费2-充值3-储值卡调整")
    private Integer changeType;
    @ApiModelProperty(notes = "单据类型名称")
    @TableField(exist = false)
    private String changeTypeName;
    @ApiModelProperty(notes = "单据编码")
    private Long billId;
    @ApiModelProperty(notes = "关联单号")
    private String billNo;
    @ApiModelProperty(notes = "备注")
    private String notes;
    @ApiModelProperty(notes = "变动实际金额")
    private BigDecimal changePayAmount;
    @ApiModelProperty(notes = "变动赠送金额")
    private BigDecimal changeCreditAmount;
    @ApiModelProperty(notes = "变动后剩余金额")
    private BigDecimal changeSurplusAmount;

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
