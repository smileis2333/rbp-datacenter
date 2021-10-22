package com.regent.rbp.api.core.storedvaluecard;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 16:47
 */
@Data
@ApiModel(description = "储值卡资产")
@TableName(value = "rbp_stored_value_card_assets")
public class StoredValueCardAssets extends Model<StoredValueCardAssets> {
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "会员编码")
    private Long storedValueCardId;

    @ApiModelProperty(notes = "实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(notes = "赠送金额")
    private BigDecimal creditAmount;

    @ApiModelProperty(notes = "累计消费金额（仅实付）")
    private BigDecimal cumulativeConsumeAmount;

    @ApiModelProperty(notes = "累计消费面额（含赠送）")
    private BigDecimal cumulativeConsumeDenomination;

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

}
