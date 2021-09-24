package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-24
 */
@Data
public class OnlineSyncGoodsStockPageDto {

    @ApiModelProperty(notes = "上传店铺库存的唯一ID")
    private String RowUniqueKey;

    @ApiModelProperty(notes = "异常编码")
    private String code;

    @ApiModelProperty(notes = "提示")
    private String msg;

}
