package com.regent.rbp.api.core.omiChannel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 线上货品信息
 * @author xuxing
 */
@Data
@ApiModel(description = "线上货品信息")
@TableName(value = "rbp_online_goods")
public class OnlineGoods {

    @TableId("id")
    private Long id;

    /** 电商平台 */
    private Long onlinePlatformId ;
    /** 系统条码 */
    private String barcode ;
    /** 重量 */
    private BigDecimal heavy;
    /** 线上条码 */
    private String onlineBarcode ;
    /** 线上货号ID */
    private String onlineGoodsId ;
    /** 线上货号 */
    private String onlineGoodsCode ;
    /** 线上货品名称 */
    private String onlineGoodsName ;
    /** 线上数量 */
    private BigDecimal onlineQuantity ;
    /** 线上货品状态 */
    private String onlineGoodsStatus ;
    /** 线上上架状态;0.未上架;1.已上架 */
    private int onlineOnsaleFlag ;
    /** 上架时间 */
    private Date onlineOnsaleDate;
    /** 线上价格 */
    private BigDecimal onlinePrice ;
    /** 异常标记;0.无异常;1.有异常 */
    private Boolean abnormalFlag ;
    /** 异常原因 */
    private String abnormalMessage ;
    /** 创建时间 */
    private Date createdTime;
    /** 更新时间 */
    private Date updatedTime;
}
