package com.regent.rbp.api.core.sendBill;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 单据物流信息 对象 rbp_send_bill_logistics
 *
 * @author chenchungui
 * @date 2021-12-16
 */
@Data
@ApiModel(description = "单据物流信息 ")
@TableName(value = "rbp_send_bill_logistics")
public class SendBillLogistics {

    @TableId
    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "物流公司")
    private Long logisticsCompanyId;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省份")
    private String province;

    @ApiModelProperty(notes = "城市")
    private String city;

    @ApiModelProperty(notes = "区/县")
    private String county;

    @ApiModelProperty(notes = "详细地址")
    private String address;

    @ApiModelProperty(notes = "收货人")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机号码")
    private String mobile;

    @ApiModelProperty(notes = "邮编")
    private String postCode;

    @ApiModelProperty(notes = "物流单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "说明")
    private String notes;

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

    public void preInsert() {
        Long userId = ThreadLocalGroup.getUserId();
        this.setCreatedBy(userId);
        this.setUpdatedBy(userId);
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }

}
