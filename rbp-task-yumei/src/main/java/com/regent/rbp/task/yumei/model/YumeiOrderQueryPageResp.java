package com.regent.rbp.task.yumei.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2022/4/3
 * @description
 */
@Data
public class YumeiOrderQueryPageResp {

    @ApiModelProperty(notes = "总数")
    private Integer total;

    @ApiModelProperty(notes = "列表")
    private List<YumeiOrderQueryPage> records;

}
