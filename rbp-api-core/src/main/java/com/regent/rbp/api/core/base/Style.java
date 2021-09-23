package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "风格 ")
@TableName(value = "rbp_style")
public class Style {
    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "编号")
    private String code;

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

    public static Style build() {
        return Style.build("");
    }

    public static Style build(String name) {
        Long userId = ThreadLocalGroup.getUserId();
        Style item = new Style();
        item.setName(name);
        item.setCode("");
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }
}
