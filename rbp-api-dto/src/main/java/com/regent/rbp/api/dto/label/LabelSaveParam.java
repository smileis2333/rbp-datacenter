package com.regent.rbp.api.dto.label;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@FromTo.List({
        @FromTo(fromField = "goodsCode", toField = "goodsId"),
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
})
@Data
public class LabelSaveParam {
    @NotBlank
    private String code;

    private String batchManagementNo;

    @ChannelCodeCheck
    private String channelCode;

    private String labelRuleNo;

    @DiscreteRange(ranges = {0,1,2,3,4},message = "单据状态(0.未使用;1.销售;2.库存;3.在途;4.作废)")
    private Integer status;

    @NotBlank
    private String goodsCode;

    @NotBlank
    private String colorCode;

    @NotBlank
    private String longName;

    @NotBlank
    private String size;

    @NotBlank
    private BigDecimal quantity;

    private String notes;

    private List<CustomizeDataDto> customizeData;

    private Long goodsId;
    private Long colorId;
    private Long longId;
    private Long sizeId;

}
