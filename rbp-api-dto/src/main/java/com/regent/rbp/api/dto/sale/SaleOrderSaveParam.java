package com.regent.rbp.api.dto.sale;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

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

    @NotBlank
    @ChannelCodeCheck
    private String channelCode;

    private String memberCode;

    @BillNo(targetTable = "rbp_sales_order_bill")
    private String originBillNo;

    @NotBlank
    private String shiftNo;

    @NotNull
    @DiscreteRange(ranges = {0, 1, 2}, message = "0.Pos;1.后台;2.第三方平台")
    private Integer origin;

    @NotNull
    @BillStatus
    private Integer status;

    @NotNull
    @DiscreteRange(ranges = {0, 1, 2, 3, 4}, message = "0.线下销售 1.全渠道发货 2.线上发货 3.线上退货 4.定金")
    private Integer billType;

    private String notes;

    @NotEmpty
    @Valid
    private List<SalesOrderBillGoodsResult> goodsDetailData;

    @Valid
    private List<SalesOrderBillPaymentResult> retailPayTypeData = Collections.emptyList();

    private List<CustomizeDataDto> customizeData;

    @Valid
    private List<EmployeeAchievement> employeeBillAchievement;

    public List<String> getAllEmployeeCodes() {
        ArrayList<String> ans = new ArrayList<>();
        if (CollUtil.isNotEmpty(employeeBillAchievement)) {
            ans.addAll(CollUtil.map(employeeBillAchievement, EmployeeAchievement::getEmployeeCode, true));
        }
        if (CollUtil.isNotEmpty(goodsDetailData)) {
            ans.addAll(goodsDetailData.stream().map(SalesOrderBillGoodsResult::getEmployeeGoodsAchievement).filter(ObjectUtil::isNotNull).flatMap(Collection::stream).map(EmployeeAchievement::getEmployeeCode).collect(Collectors.toList()));
        }
        return CollUtil.distinct(ans);
    }

}
