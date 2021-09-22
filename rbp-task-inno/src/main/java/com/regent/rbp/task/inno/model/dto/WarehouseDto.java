package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 仓库请求信息
 * @author: HaiFeng
 * @create: 2021-09-22 17:37
 */
@Data
public class WarehouseDto {

    public WarehouseDto(String id, String area_id, String code, String contact, String name, String phone, String addr) {
        this.id = id;
        this.area_id = area_id;
        this.code = code;
        this.contact = contact;
        this.name = name;
        this.phone = phone;
        this.addr = addr;
    }

    /**
     * 上传ERP仓库的唯一标示
     */
    private String id;

    /**
     * 区域ID
     */
    private String area_id;

    /**
     * 仓库编码
     */
    private String code;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 联系号码
     */
    private String phone;

    /**
     * 店铺地址
     */
    private String addr;

}
