package com.regent.rbp.api.service.base.context;

import com.regent.rbp.api.dto.base.BaseData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 基础资料 保存上下文
 */
@Data
public class BaseSaveContext {

    @ApiModelProperty("资料类别")
    private String type;

    @ApiModelProperty("数据列表")
    private List<BaseData> list;

}
