package com.regent.rbp.api.dto.base;

import lombok.Data;

import java.util.List;

@Data
public class ChannelOrganizationSaveParam {
    private String name;
    private List<ChannelOrganizationSaveParam> childrenData;
}