package com.regent.rbp.api.core.stock;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 库存调整单对象 rbp_stock_adjust_bill
 *
 * @author lzc
 * @date 2020-08-13
 */
@Data
@ApiModel(description = "库存调整单")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_stock_adjust_bill")
public class StockAdjustBill extends BillMasterData {

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "完结状态")
    private Integer finishFlag;

    @ApiModelProperty(notes = "处理状态 (0.未分析;1.分析中;2.已分析;")
    private Integer processStatus;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "科目")
    private Long subjectId;

}
