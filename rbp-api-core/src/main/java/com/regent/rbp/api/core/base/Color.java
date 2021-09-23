package com.regent.rbp.api.core.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 颜色档案
 *
 * @author chenchungui
 * @date 2021-09-09
 */
@Data
@ApiModel(description = "颜色档案")
@TableName(value = "rbp_color")
public class Color {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "颜色组编号")
    private Long groupId;

    @ApiModelProperty(notes = "状态(100-启用 101-禁用)")
    private Integer status;

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

    public static Color build() {
        return build(StrUtil.EMPTY, StrUtil.EMPTY, null);
    }

    public static Color build(String code, String name, Long groupId) {
        Long userId = ThreadLocalGroup.getUserId();
        Color item = new Color();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setName(name);
        item.setCode(code);
        item.setGroupId(groupId);
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
