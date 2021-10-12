package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 渠道业态类型
 * @author: HaiFeng
 * @create: 2021-10-12 19:36
 */
@Data
@ApiModel(description = "渠道业态类型")
@TableName(value = "rbp_business_format_type")
public class BusinessFormatType {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "业态类型编号")
    private String code;

    @ApiModelProperty(notes = "语言编号")
    private String languageCode;

    @ApiModelProperty(notes = "操作名称")
    private String name;

}
