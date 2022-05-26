package com.regent.rbp.api.core.receiveBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 指令单尺码明细 对象 rbp_notice_bill_size
 *
 * @author chenchungui
 * @date 2021-12-07
 */
@Data
@ApiModel(description = "发货单尺码实收明细 ")
@TableName(value = "rbp_receive_bill_real_size")
public class ReceiveBillRealSize extends BillGoodsSizeData {

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String goodsCode;

    @TableField(exist = false)
    private String colorNo;

    @ApiModelProperty(notes = "内长名称")
    @TableField(exist = false)
    private String longName;

    @ApiModelProperty(notes = "尺码名称")
    @TableField(exist = false)
    private String sizeName;

    public String getUniqueKey() {
        return getGoodsId()+"-"+getColorId()+"-"+getLongId()+"-"+getSizeId();
    }
}
