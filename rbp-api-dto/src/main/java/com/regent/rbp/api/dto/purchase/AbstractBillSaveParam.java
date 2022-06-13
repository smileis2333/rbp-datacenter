package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/01
 * @description
 */
@Data
public abstract class AbstractBillSaveParam {
    @ApiModelProperty(notes = "模块编号")
    @NotBlank
    private String moduleId;

    @ApiModelProperty(notes = "手工单号")
    @NotBlank
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date billDate;

    @Valid
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    @NotNull
    @BillStatus
    private Integer status;

    private String notes;

}
