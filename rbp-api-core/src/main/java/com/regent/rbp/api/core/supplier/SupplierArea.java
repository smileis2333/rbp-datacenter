package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "供应商档案 ")
@TableName(value = "rbp_supplier_area")
public class SupplierArea {
    @TableId("id")
    private Long id;

    private Long parentId;

    private Integer depth;

    private String name;

    private String columnName;

    private String orderNumber;

}
