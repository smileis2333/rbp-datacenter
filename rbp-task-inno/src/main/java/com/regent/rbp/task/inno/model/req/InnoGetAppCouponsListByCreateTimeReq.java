package com.regent.rbp.task.inno.model.req;

import lombok.Data;

/**
 * @author czd
 * @date 2021/10/23
 */
@Data
public class InnoGetAppCouponsListByCreateTimeReq{
    /**
     * 开始时间 （标准格式：2015-01-01 10:30:21)
     */
    private String beginTime;

    /**
     * 结束时间 （标准格式：2015-01-01 10:30:21)
     */
    private String endTime;

    /**
     * 页数
     */
    private int pageIndex;

    /**
     * 是否查询全部，为空或者其他值时默认查询线下券，当值为1时，查询所有优惠券
     */
    private String isQueryAll;

    /**
     * 只查询指定券类型，多个编码用英文逗号隔开，优先级最高
     */
    private String typeCodeList;
}
