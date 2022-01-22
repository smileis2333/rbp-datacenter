package com.regent.rbp.api.dto.box;

import com.regent.rbp.api.dto.validate.UniqueFields;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
public class BoxSaveParam {
    @NotEmpty
    @Valid
    @UniqueFields(fields = "manualId")
    List<BoxItem> boxData;
}
