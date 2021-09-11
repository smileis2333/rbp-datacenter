package com.regent.rbp.task.inno.model.dto;

import com.regent.rbp.task.inno.model.req.BaseRequestDto;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class ChannelDto extends BaseRequestDto {
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
}
