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
 * 模块业务类型
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
@ApiModel(description = "模块业务类型")
@TableName(value = "rbp_module_business_type")
public class ModuleBusinessType {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "模块编码")
    private String moduleId;

    @ApiModelProperty(notes = "业务类型编码")
    private Long businessTypeId;

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

    public static ModuleBusinessType build() {
        return build(StrUtil.EMPTY, null);
    }

    public static ModuleBusinessType build(String moduleId, Long businessTypeId) {
        Long userId = ThreadLocalGroup.getUserId();
        ModuleBusinessType item = new ModuleBusinessType();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setModuleId(moduleId);
        item.setBusinessTypeId(businessTypeId);

        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
