package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName(value = "rbp_supplier_nature")
public class SupplierNature {
    private Long id;

    @ApiModelProperty(notes = "名称")
    private String name;
}
