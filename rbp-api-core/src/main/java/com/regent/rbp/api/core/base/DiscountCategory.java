package com.regent.rbp.api.core.base;

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
 * @author xuxing
 */
@Data
@ApiModel(description = "折扣类别")
@TableName(value = "rbp_discount_category")
public class DiscountCategory {
    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "名称")
    private String name;

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

    public static DiscountCategory build() {
        return DiscountCategory.build("");
    }

    public static DiscountCategory build(String name) {
        Long userId = ThreadLocalGroup.getUserId();
        DiscountCategory item = new DiscountCategory();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setName(name);
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }
}
