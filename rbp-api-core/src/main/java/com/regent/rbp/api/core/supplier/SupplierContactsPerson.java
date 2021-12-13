package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "rbp_supplier_contacts_person")
public class SupplierContactsPerson {
    private Long id;

    private Long supplierId;

    private String name;

    private String position;

    private String officeTel;

    private String mobile1;

    private String mobile2;

    private String email;

    private String qq;

    private String wechat;


}
