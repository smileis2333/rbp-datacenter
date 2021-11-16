package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 单据类
 * @author: HaiFeng
 * @create: 2021-11-16 17:21
 */
@Data
public class BillMasterData {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态 (0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "处理状态 (0.未执行;1.已发货;2.已收货;)")
    private Integer processStatus;

    @ApiModelProperty(notes = "完结状态 (0.未完结 1.已完结)")
    private Integer finishFlag;

    @ApiModelProperty(notes = "审批状态 (0.待审批;1.审批中;2.已驳回;3.已通过)")
    private Integer flowStatus;

    @ApiModelProperty(notes = "流程类型 (1.审核流程;2.反审核流程;3.作废流程)")
    private Integer flowType;

    @ApiModelProperty(notes = "流程编码")
    private String flowId;

    @ApiModelProperty(notes = "科目编号")
    private Integer subjectId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;
}
