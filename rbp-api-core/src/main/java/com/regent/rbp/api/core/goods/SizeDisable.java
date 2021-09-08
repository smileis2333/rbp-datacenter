package com.regent.rbp.api.core.goods;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "尺码停用")
@TableName(value = "rbp_size_disable")
public class SizeDisable {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "颜色编码")
    private Long colorId;

    @ApiModelProperty(notes = "内长编码")
    private Long longId;

    @ApiModelProperty(notes = "尺码编码")
    private Long sizeId;
}
