package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:25
 */
@Data
public class CouponPolicyDownLoadParam {
    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;
    @ApiModelProperty(notes = "开始时间 （标准格式：2015-01-01 10:30:21)")
    private String startTime;
    @ApiModelProperty(notes = "结束时间 （标准格式：2015-01-01 10:30:21)")
    private String endTime;
    @ApiModelProperty(notes = "页数")
    private int pageIndex;
    @ApiModelProperty(notes = "是否查询全部，为空或者其他值时默认查询线下券，当值为1时，查询所有优惠券")
    private String isQueryAll;
    @ApiModelProperty(notes = "只查询指定券类型，多个编码用英文逗号隔开，优先级最高")
    private String typeCodeList;
}
