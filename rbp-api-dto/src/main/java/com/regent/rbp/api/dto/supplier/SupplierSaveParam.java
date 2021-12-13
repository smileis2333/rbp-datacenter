package com.regent.rbp.api.dto.supplier;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.AddressData;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SupplierSaveParam {

    private String supplierCode;
    private String name;
    private String fullName;
    private Integer type;
    private Integer receiveDifferentType;
    private BigDecimal receiveDifferentPercent;
    private String nature;
    private String grade;
    private String headPerson;
    private String tel1;
    private String tel2;
    private String address;
    private String fundAccountCode;
    private BigDecimal taxrate;
    private String notes;
    private String nation;
    private String province;
    private String city;
    private String county;
    private List<ContactData> contactsPerson;
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;
}
