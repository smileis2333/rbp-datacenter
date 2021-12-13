package com.regent.rbp.api.core.supplier;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName(value = "rbp_supplier_send_address")
public class SupplierSendAddress {
    @TableId("id")
    private Long id;

    private Long supplierId;

    private String code;

    private String address;

    private String contactsPerson;

    private Long postCode;

    private Long mobile;

    private Boolean defaultFlag;

    private Date createdTime;

    private Date updatedTime;

    private String nation;

    private String province;

    private String city;

    private String county;
}
