package com.regent.rbp.api.dto.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SupplierQueryParam {
    private String supplierCode;
    private String name;
    private String fullName;
    private List<Integer> type;
    private List<Integer> receiveDifferentType;
    private List<String> nature;
    private List<String> grade;
    private List<String> headPerson;
    private String address;
    private List<String> fundAccountCode;
    private List<Integer> status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;
    private List<String> fields;
    private Integer pageNo;
    private Integer pageSize;
    private String nation;
    private String region;
    private String province;
    private String city;
    private String county;

}
