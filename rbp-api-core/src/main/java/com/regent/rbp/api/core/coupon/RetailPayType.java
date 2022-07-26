package com.regent.rbp.api.core.coupon;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author LuZijian
 * @date 2021/4/14 2:06 下午
 * 支付方式
 */
@Data
@ApiModel(description = "支付方式")
@TableName(value = "rbp_retail_pay_type")
public class RetailPayType extends Model<RetailPayType> {

    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "付款方式编码")
    private String code;
    @ApiModelProperty(notes = "排序")
    private Integer sort;
    @ApiModelProperty(notes = "付款方式名称")
    private String name;
    @ApiModelProperty(notes = "零售支付平台编码")
    private Long retailPayPlatformId;

    @ApiModelProperty(notes = "零售支付平台编码")
    @TableField(exist = false)
    private String retailPayPlatform;

    @ApiModelProperty(notes = "是否可用")
    @TableField(value = "useable")
    private Integer useAble;
    @ApiModelProperty(notes = "是否显示")
    private Integer visible;
    @ApiModelProperty(notes = "是否计收")
    private Integer notIncome;
    @ApiModelProperty(notes = "是否计积分")
    private Integer notInPoint;
    @ApiModelProperty(notes = "是否系统内置")
    private Integer systemInline;


    @ApiModelProperty(notes = "pos可用")
    private Integer pos;
    @ApiModelProperty(notes = "是否还原吊牌价格")
    private Integer dpPrice;
    @ApiModelProperty(notes = "限制比例")
    private Integer limitRatio;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
