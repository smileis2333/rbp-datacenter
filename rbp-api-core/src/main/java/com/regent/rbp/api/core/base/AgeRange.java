package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "适用年龄")
@TableName(value = "rbp_age_range")
@NoArgsConstructor
public class AgeRange {
    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "最小年龄")
    private int minAge;

    @ApiModelProperty(notes = "最大年龄")
    private int maxAge;

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

    public static AgeRange build(int minAge, int maxAge) {
        Long userId = ThreadLocalGroup.getUserId();
        AgeRange item = new AgeRange();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setMinAge(minAge);
        item.setMaxAge(maxAge);
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

}
