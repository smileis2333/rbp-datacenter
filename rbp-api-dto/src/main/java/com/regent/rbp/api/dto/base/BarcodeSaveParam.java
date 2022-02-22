package com.regent.rbp.api.dto.base;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeSaveParam {
    @NotEmpty
    @UniqueElements(message = "条形码不允许重复")
    @Valid
    private List<BarcodeItem> barcodeData;
}
