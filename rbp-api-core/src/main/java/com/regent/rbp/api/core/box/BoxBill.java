package com.regent.rbp.api.core.box;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author huangjie
 * @date : 2022/01/20
 * @description
 */
@Data
@TableName(value = "rbp_box_bill")
public class BoxBill {
    private Long id;
    private Long boxId;
    private String moduleId;
    private Long billId;
    private Long channelId;
    private Long toChannelId;
    private Long logisticsCompanyId;
    private String logisticsBillCode;
    private Long supplierId;
    private Integer status;
    private Long createdBy;
    private Date createdTime;

}
