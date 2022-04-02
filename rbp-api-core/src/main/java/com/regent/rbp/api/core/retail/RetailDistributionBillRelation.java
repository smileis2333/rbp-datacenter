package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
@ApiModel(description = "全渠道配单关联订单 ")
@TableName(value = "rbp_retail_distribution_bill_relation")
public class RetailDistributionBillRelation extends Model<RetailDistributionBillRelation> {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "全渠道订单ID")
    private Long retailOrderBillId;

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

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert() {
        setCreatedBy(ThreadLocalGroup.getUserId());
        setCreatedTime(null);
    }


}
