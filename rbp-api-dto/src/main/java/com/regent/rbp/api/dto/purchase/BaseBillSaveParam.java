package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class BaseBillSaveParam {
    @NotBlank
    private String moduleId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotNull
    @BillStatus
    private Integer status;

    private String notes;

    private List<CustomizeDataDto> customizeData;
}
