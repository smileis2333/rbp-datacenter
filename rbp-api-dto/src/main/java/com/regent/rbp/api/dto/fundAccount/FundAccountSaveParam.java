package com.regent.rbp.api.dto.fundAccount;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.Channelorganization;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 资金号保存参数
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Data
public class FundAccountSaveParam {

    @ApiModelProperty("资金号编号")
    @NotBlank
    private String code;

    @ApiModelProperty("资金号名称")
    @NotBlank
    private String name;

    @ApiModelProperty("单据状态(0.未审核,1.已审核)")
    @NotNull
    @DiscreteRange(ranges = {0, 1}, message = "单据状态(0.未审核,1.已审核)")
    private Integer status;

    @ApiModelProperty("备注")
    private String notes;

    @ApiModelProperty("上级资金号编号")
    private String parentCode;

    @ApiModelProperty("法人")
    private String legalPerson;

    @ApiModelProperty("信用额度")
    private BigDecimal credit;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税号")
    private String taxNumber;

    @ApiModelProperty("类型(0.全部；1.应收；2.应付)")
    private Integer type;

    @ApiModelProperty("组织架构")
    private Channelorganization organization;

    @ApiModelProperty("价格政策")
    private List<PricePolicy> pricePolicy;

    @ApiModelProperty("品牌价格政策")
    private List<BrandPricePolicy> brandPricePolicy;

    @ApiModelProperty("银行账号")
    private BankAccount bankAccount;

    @ApiModelProperty("自定义字段")
    private List<CustomizeDataDto> customizeData;
}
