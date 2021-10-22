package com.regent.rbp.task.inno.model.req;

import lombok.Data;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:44
 */
@Data
public class InnoGetAppCouponsListByCreateTimeReq extends BaseRequestDto{
    /**
     * 开始时间 （标准格式：2015-01-01 10:30:21)
     */
    private String Begintime;

    /**
     * 结束时间 （标准格式：2015-01-01 10:30:21)
     */
    private String Endtime;

    /**
     * 页数
     */
    private int PageIndex;

    /**
     * 是否查询全部，为空或者其他值时默认查询线下券，当值为1时，查询所有优惠券
     */
    private String IsQueryAll;

    /**
     * 只查询指定券类型，多个编码用英文逗号隔开，优先级最高
     */
    private String typeCodeList;
}
