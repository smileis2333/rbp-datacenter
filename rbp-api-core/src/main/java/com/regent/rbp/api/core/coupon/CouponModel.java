package com.regent.rbp.api.core.coupon;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author LuZijian
 * @date 2022/1/20 2:58 下午
 */
@Data
@ApiModel(description = "券模版")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_coupon_model")
public class CouponModel extends Model<CouponModel> {

    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "类型 1抵用券2折扣券")
    private Integer type;
    @ApiModelProperty(notes = "优惠券编号")
    private String modelNo;
    @ApiModelProperty(notes = "优惠券名字")
    private String modelName;
    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "有效期类型 1固定时间2相对时间")
    private Integer dateType;
    @ApiModelProperty(notes = "生效日期")
    private Date effectiveDate;
    @ApiModelProperty(notes = "失效日期")
    private Date expirationDate;
    @ApiModelProperty(notes = "有效日期")
    @TableField(exist = false)
    private Integer validityDate;

    @ApiModelProperty(notes = "发行数量")
    private Integer number;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "领取限制 每个会员可拥有多少张券(不管状态以及是否使用)")
    private Integer memberLimit;
    @ApiModelProperty(notes = "是否导购发券0否1是")
    private Integer employeeCreate;


    @ApiModelProperty(notes = "单据状态;0.未审核,1.已审核,2.反审核,3.已作废")
    private Integer status;
    @ApiModelProperty(notes = "券来源 -1.全部 1.pos,2.英朗,3.微盟,4.有赞5.yike")
    private Integer origin;
    @ApiModelProperty(notes = "券来源 -1.全部 1.pos,2.英朗,3.微盟,4.有赞5.yike")
    @TableField(exist = false)
    private String originName;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "券类型")
    @TableField(exist = false)
    private String bonusType;

    @ApiModelProperty(notes = "支付方式编号")
    @TableField(exist = false)
    private String paymentCode;

    @ApiModelProperty(notes = "折扣")
    @TableField(exist = false)
    private String discount;

    @ApiModelProperty(notes = "允许使用券的最大金额")
    @TableField(exist = false)
    private String maxAmount;

    @ApiModelProperty(notes = "优惠券金额")
    @TableField(exist = false)
    private String typeMoney;

    @ApiModelProperty(notes = "使用店铺范围")
    @TableField(exist = false)
    private List<CouponRuleChannelRange> couponRuleChannelRangeList;
    @ApiModelProperty(notes = "使用货品范围")
    @TableField(exist = false)
    private List<CouponRuleGoodsRange> couponRuleGoodsRangeList;
    @ApiModelProperty(notes = "作废人")
    private Long cancelBy;
    @ApiModelProperty(notes = "作废时间")
    private Date cancelTime;
}
