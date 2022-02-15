package com.regent.rbp.api.service.label.context;

import com.regent.rbp.api.core.label.Label;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class LabelSaveContext {
    private Label label;
    private List<CustomizeDataDto> customizeData;
}
