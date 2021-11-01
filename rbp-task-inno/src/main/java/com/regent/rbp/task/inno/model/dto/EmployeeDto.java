package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
@Data
public class EmployeeDto {
    /**
     * 员工编码
     */
    private String staff_code;

    /**
     * 员工名称
     */
    private String staff_name;

    /**
     * 店铺编码
     */
    private String store_code;
    /**
     * 电话
     */
    private String staff_mobile;
    /**
     * 微信Openid
     */
    private String openid;
    /**
     * 最后修改时间
     */
    private String last_time;
    /**
     * 是否启用状态，0失效，1启用，不传默认是启用
     */
    private String IsEnabled;

    /**
     * 是否启用状态，0失效，1启用，不传默认是启用
     */
    private Integer Status;

    public EmployeeDto(String staff_code, String staff_name, String store_code, String staff_mobile, String openid, String last_time, String isEnabled, Integer status) {
        this.staff_code = staff_code;
        this.staff_name = staff_name;
        this.store_code = store_code;
        this.staff_mobile = staff_mobile;
        this.openid = openid;
        this.last_time = last_time;
        IsEnabled = isEnabled;
        Status = status;
    }
}
