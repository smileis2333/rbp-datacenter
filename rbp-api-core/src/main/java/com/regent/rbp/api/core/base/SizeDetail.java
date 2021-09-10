package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-10
 */
@Data
@ApiModel(description = "尺码明细档案")
@TableName(value = "rbp_size_detail")
public class SizeDetail {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "尺码分类编码")
    private Long sizeClassId;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "横排字段名称")
    private String fieldName;

    @ApiModelProperty(notes = "序号")
    private Integer orderNumber;

    public static SizeDetail build() {
        return SizeDetail.build("", null);
    }

    public static SizeDetail build(String name, Long sizeClassId) {
        SizeDetail item = new SizeDetail();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setName(name);
        item.setSizeClassId(sizeClassId);
        return item;
    }
}
