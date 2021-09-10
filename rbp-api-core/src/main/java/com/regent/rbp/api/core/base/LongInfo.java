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
 * @author chenchungui
 * @date 2021-09-10
 */
@Data
@ApiModel(description = "内长")
@TableName(value = "rbp_long")
public class LongInfo {

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

    public static LongInfo build() {
        return build("");
    }

    public static LongInfo build(String name) {
        long userId = ThreadLocalGroup.getUserId();
        LongInfo item = new LongInfo();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setName(name);
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }

}
