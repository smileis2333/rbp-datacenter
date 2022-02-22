package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 会员
 * @author: HaiFeng
 * @create: 2021-09-24 13:32
 */
@Data
public class InnoMemberDto {
    /**
     * 序号（1，2，3）区分上传的会员
     */
    private Integer rowIndex;

    /**
     * 用户名，默认跟手机号一致
     */
    private String user_name;

    /**
     * 店铺编码
     */
    private String store_code;

    /**
     * 会员卡号
     */
    private String card_no;

    /**
     * 用户别名
     */
    private String nick_name;

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
    private String mobile_no;

    /**
     * 创建时间 2016-01-01 10:00:00
     */
    private String create_date;

    /**
     * 修改时间 2016-01-01 10:00:00
     */
    private String modify_date;

    /**
     * 等级代码，用于设置对应等级
     */
    private String rank_code;

    /**
     * 店员编码
     */
    private String staff_code;

    /**
     * 历史积分
     */
    private Integer history_point;
}
