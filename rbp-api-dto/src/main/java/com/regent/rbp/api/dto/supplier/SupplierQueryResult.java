package com.regent.rbp.api.dto.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.channel.ChannelBarrio;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SupplierQueryResult {
    private String supplierCode;
    private String name;
    private String fullName;
    private Integer type;
    private Integer receiveDifferentType;
    private Integer receiveDifferentPercent;
    private String nature;
    private String grade;
    private String headPerson;
    private String tel1;
    private String tel2;
    private String address;
    private String fundAccountCode;
    private BigDecimal taxrate;
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    private ChannelBarrio channelBarrio;
    private List<ContactData> contactsPerson;
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;

}