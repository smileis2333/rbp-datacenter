package com.regent.rbp.api.dto.supplier;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ContactData {
    @NotEmpty
    private String name;
    private String position;
    private String officeTel;
    private String mobile1;
    private String mobile2;
    private String email;
    private String qq;
    private String wechat;
}
