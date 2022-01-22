package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderDownloadOnlineOrderParam {

    @ApiModelProperty(notes = "平台编码")
    private String onlinePlatformCode;

    @ApiModelProperty(notes = "开始时间（时间标准化：2021-01-01 00:00:01）")
    private Date beginTime;

    @ApiModelProperty(notes = "结束时间（时间标准化：2021-01-02 00:00:01）")
    private Date endTime;

    @ApiModelProperty(notes = "订单SN(订单编号 例如： “201510010003,201510010004”）")
    private String order_sn_list;

}
