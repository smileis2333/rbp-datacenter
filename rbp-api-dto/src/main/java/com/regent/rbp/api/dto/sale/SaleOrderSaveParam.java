package com.regent.rbp.api.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillNo;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.ConflictManualIdCheck;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单 新增/修改
 * @author: HaiFeng
 * @create: 2021-11-09 17:11
 */
@Data
public class SaleOrderSaveParam {

    @NotBlank
    private String billNo;

    @ConflictManualIdCheck(targetTable = "rbp_sales_order_bill")
    private String manualId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotBlank
    @ChannelCodeCheck
    private String saleChannelCode;

    @ChannelCodeCheck
    private String channelCode;

    private String memberCode;

    @BillNo(targetTable = "rbp_sales_order_bill")
    private String originBillNo;

    private String shiftNo;

    @NotNull
    @Range(min = 0,max = 2,message = "(0.Pos;1.后台;2.第三方平台)") // todo extract annotation to prevent future change
    private Integer origin;

    @NotNull
    @BillStatus
    private Integer status;

    @NotNull
    @Range(min = 0,max = 4,message = "(0.线下销售 1.全渠道发货 2.线上发货 3.线上退货 4.定金)") // todo extract annotation to prevent future change
    private Integer billType;

    @NotNull(message = "{javax.validation.constraints.DecimalMax.message}")
    private String notes;

    @NotEmpty
    private List<SalesOrderBillGoodsResult> goodsDetailData;

    private List<SalesOrderBillPaymentResult> retailPayTypeData;

    private List<CustomizeDataDto> customizeData;
}
