package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(description = "价格类型")
@TableName(value = "rbp_price_type")
public class PriceType {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库默认时间
     */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    /**
     * 数据库默认时间
     */
    private Date updatedTime;

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setCreatedBy(ThreadLocalGroup.getUserId());
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setCreatedTime(date);
        setUpdatedTime(date);
    }

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }

}
