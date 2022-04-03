package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenchungui
 * @date 2022/4/3
 * @description
 */
@Data
public class YumeiOrderQueryReq {

    @ApiModelProperty(notes = "门店编号")
    private String storeNo;

    @ApiModelProperty(notes = "订单来源(1：美人计会员商城、2：酒会员商城、3：丽晶）")
    private Integer orderSource;

    @ApiModelProperty(notes = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "结束时间")
    private Date endTime;

    @ApiModelProperty(notes = "订单编号")
    private String outOrderNo;

    @ApiModelProperty(notes = "当前页码(不传值默认从1页开始)")
    private Integer pageNum;

    @ApiModelProperty(notes = "每页记录数(每页返回的数据条数，输入值范围1~100，默认值为10)")
    private Integer pageSize;

    @ApiModelProperty(notes = "订单状态（1：待支付、2：待发货、3：待收货、4：已取消、5：确认收货、6：已退款、7：申请售后、8：平台介入）")
    private Integer status;

}
