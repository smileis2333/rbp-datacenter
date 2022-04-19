package com.regent.rbp.task.yumei.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 玉美退货物流上传
 * @author: HaiFeng
 * @create: 2022/4/19 18:57
 */
@Data
public class YumeiRefund {

    @ApiModelProperty(notes = "门店编号")
    private String storeNo;

    @ApiModelProperty(notes = "订单编号")
    private String outOrderNo;

    @ApiModelProperty(notes = "订单子项信息")
    private List<YumeiRefundItems> data;

}
