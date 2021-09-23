package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 全渠道订单付款信息 对象 rbp_retail_order_bill_payment_info
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
@ApiModel(description = "全渠道订单付款信息 ")
@TableName(value = "rbp_retail_order_bill_payment_info")
public class RetailOrderBillPaymentInfo {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "零售付款方式")
    private Long retailPaymentId;

    @ApiModelProperty(notes = "交易号")
    private String transactionNo;

    @ApiModelProperty(notes = "卡号")
    private String cardNo;

    @ApiModelProperty(notes = "金额")
    private BigDecimal amount;

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

    public static RetailOrderBillPaymentInfo build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailOrderBillPaymentInfo item = new RetailOrderBillPaymentInfo();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
