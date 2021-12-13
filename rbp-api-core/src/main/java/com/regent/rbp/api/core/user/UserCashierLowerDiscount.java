package com.regent.rbp.api.core.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author chenchungui
 * @Date 2021/12-10
 */
@Data
@ApiModel(description = "收银员分类最低折扣")
@TableName(value = "rbp_user_cashier_lower_discount")
public class UserCashierLowerDiscount {

    @TableId("id")
    @ApiModelProperty(notes = "ID")
    private Long id;

    @ApiModelProperty(notes = "用户编码;关联表rbp_user")
    private Long userId;

    @ApiModelProperty(notes = "货品类别;关联表rbp_category")
    private Long categoryId;

    @ApiModelProperty(notes = "货品年份;关联表rbp_year")
    private Long yearId;

    @ApiModelProperty(notes = "最低折扣")
    private BigDecimal lowerDiscount;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    public static UserCashierLowerDiscount build() {
        UserCashierLowerDiscount entity = new UserCashierLowerDiscount();
        entity.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        Long userId = ThreadLocalGroup.getUserId();
        entity.setCreatedBy(userId);
        entity.setUpdatedBy(userId);

        return entity;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
