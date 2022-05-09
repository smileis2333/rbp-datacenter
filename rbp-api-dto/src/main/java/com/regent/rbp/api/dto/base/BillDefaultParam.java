package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class BillDefaultParam extends DefaultParam {
    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @JsonIgnore
    private Long billId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    private List<Integer> status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;

    @ApiModelProperty(notes = "审核日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateStart;

    @ApiModelProperty(notes = "审核日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;

}
