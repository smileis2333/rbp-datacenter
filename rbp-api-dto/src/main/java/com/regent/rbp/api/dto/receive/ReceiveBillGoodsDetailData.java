package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillGoodsDetailData extends BillGoodsDetailData {
    @NotBlank
    private String priceType;
    @JsonIgnore
    private Long goodsId;
    @JsonIgnore
    private Long columnId;
}
