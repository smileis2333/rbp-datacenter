package com.regent.rbp.api.dto.supplier;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.channel.PhysicalRegion;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SupplierSaveParam {

    @NotEmpty(message = "供应商编号不能为空")
    private String supplierCode;
    @NotEmpty(message = "供应商简称不能为空")
    private String name;
    @NotEmpty(message = "供应商名称不能为空")
    private String fullName;
    @Range(min = 0, max = 1, message = "供应商分类0.成衣供应商;1.物料供应商")
    private Integer type;
    @Range(min = 1, max = 3, message = "来货超差类型1.货品;2.货品+颜色;3.货品+颜色+尺码;")
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
    private PhysicalRegion physicalRegion;
    private List<ContactData> contactsPerson;
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;
}
