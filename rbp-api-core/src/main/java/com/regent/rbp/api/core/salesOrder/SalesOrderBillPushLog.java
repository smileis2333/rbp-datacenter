package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单推送日志
 * @author: HaiFeng
 * @create: 2022/5/27 11:20
 */
@Data
@ApiModel(description = "销售单推送日志 ")
@TableName(value = "rbp_sales_order_bill_push_log")
public class SalesOrderBillPushLog {

    public SalesOrderBillPushLog(){}

    public SalesOrderBillPushLog(Long id, String billNo, String url, String requestParam, String result, Integer sucess) {
        this.id = id;
        this.billNo = billNo;
        this.url = url;
        this.requestParam = requestParam;
        this.result = result;
        this.sucess = sucess;
    }

    @TableId
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "推送接口地址")
    private String url;

    @ApiModelProperty(notes = "请求参数")
    private String requestParam;

    @ApiModelProperty(notes = "返回信息")
    private String result;

    @ApiModelProperty(notes = "推送是否成功 0.失败，1.成功")
    private Integer sucess;

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
