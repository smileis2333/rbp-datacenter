package com.regent.rbp.api.dto.label;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.Dictionary;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
@Valid
public class LabelSaveParam {
    @NotBlank
    private String code;

    private String batchManagementNo;

    @ChannelCodeCheck
    private String channelCode;

    private String labelRuleNo;

    @NotNull
    @DiscreteRange(ranges = {0,1,2,3,4},message = "单据状态(0.未使用;1.销售;2.库存;3.在途;4.作废)")
    private Integer status;

    @NotBlank
    private String goodsCode;

    @NotBlank
    @Dictionary(targetTable = "rbp_color",targetField = "code")
    private String colorCode;

    @NotBlank
    @Dictionary(targetTable = "rbp_long",targetField = "name")
    private String longName;

    @NotBlank
    private String size;

    @NotNull
    private BigDecimal quantity;

    private String notes;

    private List<CustomizeDataDto> customizeData;

    private Long sizeId;

}
