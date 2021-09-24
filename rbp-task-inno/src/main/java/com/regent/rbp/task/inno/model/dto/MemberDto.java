package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 会员
 * @author: HaiFeng
 * @create: 2021-09-24 13:32
 */
@Data
public class MemberDto {
    /**
     * 序号（1，2，3）区分上传的会员
     */
    private Integer rowIndex;

    /**
     * 用户名，默认跟手机号一致
     */
    private String userName;

    /**
     * 店铺编码
     */
    private String storeCode;

    /**
     * 会员卡号
     */
    private String cardNo;

    /**
     * 用户别名
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 男或女
     */
    private String sex;

    /**
     * 生日 1990-01-01
     */
    private String birthday;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 创建时间 2016-01-01 10:00:00
     */
    private String createDate;

    /**
     * 修改时间 2016-01-01 10:00:00
     */
    private String modifyDate;

    /**
     * 等级代码，用于设置对应等级
     */
    private String rankCode;

    /**
     * 店员编码
     */
    private String staffCode;

    /**
     * 历史积分
     */
    private Integer historyPoint;
}
