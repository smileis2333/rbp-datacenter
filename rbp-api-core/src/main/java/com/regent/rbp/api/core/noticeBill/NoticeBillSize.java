package com.regent.rbp.api.core.noticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 指令单尺码明细 对象 rbp_notice_bill_size
 *
 * @author chenchungui
 * @date 2021-12-07
 */
@Data
@ApiModel(description = "指令单尺码明细 ")
@TableName(value = "rbp_notice_bill_size")
public class NoticeBillSize extends BillGoodsSizeData {

    @ApiModelProperty(notes = "指令单欠数")
    private BigDecimal oweQuantity;

}
