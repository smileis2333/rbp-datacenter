package com.regent.rbp.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @Description 员工档案保存参数
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Data
public class EmployeeSaveParam {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private String sexName;
    private String mobile;
    private String address;
    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date entryDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date leaveDate;
    private String jobNumber;
    private String notes;
    private String positionName;
    private Integer workStatus;
    @NotBlank
    @ChannelCodeCheck
    private String channelCode;
    private List<CustomizeDataDto> customizeData;
}
