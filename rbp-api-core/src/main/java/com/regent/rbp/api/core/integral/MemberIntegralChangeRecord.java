package com.regent.rbp.api.core.integral;

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
 * @Date 2021/10/20 14:25
 */
@Data
@ApiModel(description = "会员积分变动记录")
@TableName(value = "rbp_member_integral_change_record")
public class MemberIntegralChangeRecord extends Model<MemberIntegralChangeRecord> {
    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "会员编码")
    private Long memberCardId;
    @ApiModelProperty(notes = "会员编号")
    @TableField(exist = false)
    private String memberCardNo;
    @ApiModelProperty(notes = "会员名称")
    @TableField(exist = false)
    private String memberCardName;
    @ApiModelProperty(notes = "变动类型 1-消费2-充值3-储值卡调整")
    private Integer changeType;
    @ApiModelProperty(notes = "变动类型名称")
    @TableField(exist = false)
    private String changeTypeName;
    @ApiModelProperty(notes = "单据编码")
    private Long billId;
    @ApiModelProperty(notes = "关联单号")
    private String billNo;
    @ApiModelProperty(notes = "备注")
    private String notes;
    @ApiModelProperty(notes = "变动积分")
    private BigDecimal changeIntegral;
    @ApiModelProperty(notes = "变动后剩余积分")
    private BigDecimal changeSurplusIntegral;
    @ApiModelProperty(notes = "创建人")
    private Long createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;
}
