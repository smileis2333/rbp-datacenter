package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName(value = "rbp_supplier_send_address")
public class SupplierSendAddress {

    private Long supplierId;

    private String code;

    private String address;

    private String contactsPerson;

    private Long postCode;

    private Long mobile;

    private Boolean defaultFlag;

    private Date createdTime;

    private Date updatedTime;

}
