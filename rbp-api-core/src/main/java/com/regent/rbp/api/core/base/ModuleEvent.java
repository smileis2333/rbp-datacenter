package com.regent.rbp.api.core.base;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 模块事件模型
 * @author xuxing
 */
@Data
@ApiModel(description = "模块事件")
@TableName(value = "rbp_module_event")
public class ModuleEvent {
    /** 编码 */
    @TableId
    @ApiModelProperty(notes = "编码")
    private Long id ;
    /** 模块编号 */
    @ApiModelProperty(notes = "模块编号")
    private String moduleId ;
    /** 基础模块编号 */
    @ApiModelProperty(notes = "任务名称")
    private String baseModuleId ;
    /** 事件类型:CREATE,UPDATE,DELETE,CHECK,UNCHECK,CANCEL,UNCANCEL */
    @ApiModelProperty(notes = "事件类型:CREATE,UPDATE,DELETE,CHECK,UNCHECK,CANCEL,UNCANCEL")
    private String eventType ;
    /** 内容编号;通常是单据ID */
    @ApiModelProperty(notes = "内容编号;通常是单据ID")
    private String contentId ;
    /** 来源模块编号;解决工作台创建的单据 */
    @ApiModelProperty(notes = "来源模块编号")
    private String fromModuleId ;
    /** 事件说明 */
    @ApiModelProperty(notes = "事件说明")
    private String notes ;
    /** 创建人 */
    @ApiModelProperty(notes = "创建人")
    private Long createdBy ;
    /** 创建时间 */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;
    /** 更新人 */
    @ApiModelProperty(notes = "更新人")
    private Long updatedBy ;
    /** 更新时间 */
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}