package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 班次
 * @author: HaiFeng
 * @create: 2021-11-10 14:19
 */
@Data
@ApiModel(description = "班次 ")
@TableName(value = "rbp_pos_class")
public class PosClass {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "顺序")
    @TableId("`index`")
    private Integer index;

    @ApiModelProperty(notes = "创建人")
    private Long created_by;

    @ApiModelProperty(notes = "创建时间")
    private Date created_time;

    @ApiModelProperty(notes = "更新人")
    private Long updated_by;

    @ApiModelProperty(notes = "更新时间")
    private Date updated_time;
}
