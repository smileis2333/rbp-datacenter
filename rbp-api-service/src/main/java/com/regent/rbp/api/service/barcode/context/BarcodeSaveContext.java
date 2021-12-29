package com.regent.rbp.api.service.barcode.context;

import com.regent.rbp.api.core.base.Barcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeSaveContext {

    private List<Barcode> barcodes;

}
