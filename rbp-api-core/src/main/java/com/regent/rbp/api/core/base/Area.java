package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 单据物流地区
 * @Author shaoqidong
 * @Date 2020/9/16
 **/
@Data
@TableName(value = "rbp_area")
public class Area{

    @ApiModelProperty(notes = "主键id")
    private Long id;

    @ApiModelProperty(notes = "父级id,一级则为0")
    private Long parentId;

    @ApiModelProperty(notes = "深度")
    private Integer depth;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "字段名")
    private String columnName;

    @ApiModelProperty(notes = "排序")
    private String orderNumber;
}
