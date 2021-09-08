package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "供应商档案 ")
@TableName(value = "rbp_supplier")
public class Supplier {
    @TableId("id")
    @ApiModelProperty(notes = "ID")
    private Long id;

    @ApiModelProperty(notes = "货号")
    private String code;

    @ApiModelProperty(notes = "品名")
    private String name;

}
