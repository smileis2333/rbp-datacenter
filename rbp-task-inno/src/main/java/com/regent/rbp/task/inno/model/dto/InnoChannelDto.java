package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuxing
 */
@Data
public class InnoChannelDto {

    public InnoChannelDto(String id, String name, String phone, String contact, String store_code, String addr, String isCloseSelfGet, String isEnabled, String agent_code, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.contact = contact;
        this.store_code = store_code;
        this.addr = addr;
        this.IsCloseSelfGet = isCloseSelfGet;
        this.IsEnabled = isEnabled;
        this.Agent_code = agent_code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 上传ERP店铺的唯一标示
     */
    private String id;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 联系号码
     */
    private String phone;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 店铺编码
     */
    private String store_code;
    /**
     * 店铺地址
     */
    private String addr;
    /**
     * 是否关闭到店自提，值为1的时候关闭
     */
    private String IsCloseSelfGet;
    /**
     * 是否启用状态，0失效，1启用，不传默认是启用
     */
    private String IsEnabled;

    /**
     * 渠道代码
     */
    private String Agent_code;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;
}
