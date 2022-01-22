package com.regent.rbp.api.dto.box;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@FromTo.List({
        @FromTo(fromField = "channelCode", toField = "channelId"),
        @FromTo(fromField = "supplierCode", toField = "supplierId"),
        @FromTo(fromField = "distributionTypeCode", toField = "distributionTypeId"),
})
@Data
public class BoxItem {
    @NotBlank
    private String code;

    private String manualId;

    private String batchNumber;

    private String channelCode;

    private String supplierCode;

    private String distributionTypeCode;

    @DiscreteRange(ranges = {0, 1}, message = "入参非法，合法输入0.普通箱;1.配码箱")
    private Integer type;

    @DiscreteRange(ranges = {1, 2, 3, 4}, message = "入参非法，合法输入1.空;2.库存;3.在途;4.作废")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buildDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    @Range(min = 1)
    private Integer validateDay;

    private String notes;

    private BigDecimal weight;

    @Valid
    @NotEmpty
    private List<BoxDetailItem> boxDetail;

    @Null
    private Long channelId;

    @Null
    private Long supplierId;

    @Null
    private Long distributionTypeId;

}
