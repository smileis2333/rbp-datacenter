package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
@TableName(value = "rbp_retail_distribution_bill")
public class RetailDistributionBill {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "配货渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "收货渠道编码")
    private Long toChannelId;

    @ApiModelProperty(notes = "接单状态 (0.未接单,1.已接单)")
    private Integer acceptOrderStatus;

    @ApiModelProperty(notes = "打印状态 (0.未打印,1.已打印)")
    private Integer printStatus;

    @ApiModelProperty(notes = "打印次数")
    private Integer printCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "打印时间")
    private Date printTime;

    @ApiModelProperty(notes = "单据状态 (0.未审核,1.已审核,2.反审核,3.已作废,4.已验货,5.已发货,6.已超时)")
    private Integer status;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @ApiModelProperty(notes = "审核人名称")
    @TableField(exist = false)
    private String checkByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效人名称")
    @TableField(exist = false)
    private String cancelByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核人名称")
    @TableField(exist = false)
    private String uncheckByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "单据货品明细")
    @TableField(exist = false)
    private List<RetailDistributionBillGoods> billGoodsList;

    @ApiModelProperty(notes = "全渠道配单物流信息")
    @TableField(exist = false)
    private List<RetailDistributionBillLogisticsInfo> billLogisticsInfoList;

    @ApiModelProperty(notes = "全渠道配单顾客信息")
    @TableField(exist = false)
    private RetailDistributionBillCustomerInfo billCustomerInfo;

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

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;

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
