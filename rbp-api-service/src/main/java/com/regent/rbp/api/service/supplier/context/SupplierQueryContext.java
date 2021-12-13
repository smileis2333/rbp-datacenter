package com.regent.rbp.api.service.supplier.context;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SupplierQueryContext {
    private String supplierCode;
    private String name;
    private String fullName;
    private List<Integer> type;
    private List<Integer> receiveDifferentType;
    private List<Long> natureIds;
    private List<Long> gradeIds;
    private List<String> headPerson;
    private String address;
    private List<Long> fundAccountIds;
    private List<Integer> status;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private List<String> fields;
    private Integer pageNo;
    private Integer pageSize;
    private String nation;
    private String region;
    private String province;
    private String city;
    private String county;

    public boolean isValid() {
        return !isNotValid();
    }

    private boolean isNotValid() {
        return (fundAccountIds != null && fundAccountIds.isEmpty()) ||
                (gradeIds != null && gradeIds.isEmpty()) ||
                (natureIds != null && natureIds.isEmpty()) ||
                (status != null && (status.stream().filter(e -> e < 0 || e > 3).count() > 0)) ||
                (receiveDifferentType != null && (receiveDifferentType.stream().filter(e -> e < 0 || e > 2)).count() > 0);
    }
}
