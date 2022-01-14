package com.regent.rbp.api.dto.supplier;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SupplierSaveParam {

    @NotBlank
    private String supplierCode;

    @NotBlank
    private String name;

    @NotBlank
    private String fullName;

    @NotNull
    @BillStatus
    private Integer status;

    @DiscreteRange(ranges = {0,1},message = "入参非法，合法输入0.成衣供应商;1.物料供应商")
    private Integer type;

    @DiscreteRange(ranges = {1,2,3},message = "入参非法，合法输入1.货品;2.货品+颜色;3.货品+颜色+尺码")
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
    @Valid
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;
}
