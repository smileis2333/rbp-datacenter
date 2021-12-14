package com.regent.rbp.api.core.fundAccount;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金号价格政策 对象 rbp_fund_account_price_policy
 * 
 * @author liuzhicheng
 * @date 2020-09-04
 */
@Data
@ApiModel(description="资金号价格政策 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_fund_account_price_policy")
public class FundAccountPricePolicy{

    @ApiModelProperty(notes = "编码")
    private Long id;


    @ApiModelProperty(notes = "资金号")
    private Long fundAccountId;


    @ApiModelProperty(notes = "价格类型编码 关联价格类型rbp_price_type")
    private Long priceTypeId;


    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库默认时间
     */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    /**
     * 数据库默认时间
     */
    private Date updatedTime;

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setCreatedBy(ThreadLocalGroup.getUserId());
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setCreatedTime(date);
        setUpdatedTime(date);
    }

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }

}
