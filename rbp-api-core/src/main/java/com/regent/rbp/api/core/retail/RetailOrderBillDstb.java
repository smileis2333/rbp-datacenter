package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 分销信息
 * @author: HaiFeng
 * @create: 2022/4/7 17:25
 */
@Data
@TableName(value = "rbp_retail_order_bill_dstb")
public class RetailOrderBillDstb {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "订单Id")
    private Long billId;

    @ApiModelProperty(notes = "分销等级")
    private Integer level;

    @ApiModelProperty(notes = "分销员")
    private Long dstbId;

    @ApiModelProperty(notes = "分销员手机")
    private String phone;

    @ApiModelProperty(notes = "分销员关联卡号")
    private Long memberId;

    @ApiModelProperty(notes = "提成类型")
    private Integer commType;

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

    public static RetailOrderBillDstb build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailOrderBillDstb item = new RetailOrderBillDstb();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

}
