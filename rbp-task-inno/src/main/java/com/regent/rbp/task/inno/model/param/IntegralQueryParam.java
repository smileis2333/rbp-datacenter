package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/20 16:25
 */
@Data
public class IntegralQueryParam {
    @ApiModelProperty(notes = "排序 asc升序 desc降序 （默认按时间升序）")
    private String sort;
    @ApiModelProperty(notes = "开始时间 （核销日期(制单日期)）")
    private Date startDate;
    @ApiModelProperty(notes = "结束时间 （核销日期(制单日期)）")
    private Date endDate;
    @ApiModelProperty(notes = "VIP卡号")
    private String vip;
    @ApiModelProperty(notes = "页码")
    private int page;
    @ApiModelProperty(notes = "每页单数")
    private int pageSize;
}
