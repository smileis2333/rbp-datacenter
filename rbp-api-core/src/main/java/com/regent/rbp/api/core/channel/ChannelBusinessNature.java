package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道经营性质
 * @author: HaiFeng
 * @create: 2021-09-11 14:51
 */
@Data
    @ApiModel(description = "渠道经营性质")
@TableName(value = "rbp_channel_business_nature")
public class ChannelBusinessNature {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "经营性质编号")
    private String businessNatureTypeCode;

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

    public static Brand build() {
        return Brand.build("", "");
    }

    public static Brand build(String code, String name) {
        Long userId = ThreadLocalGroup.getUserId();
        Brand item = new Brand();
        item.setCode(code);
        item.setName(name);
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }
}
