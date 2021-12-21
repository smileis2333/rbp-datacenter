package com.regent.rbp.api.core.receiveBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
@ApiModel(description = "收货单货品实收明细 ")
@TableName(value = "rbp_receive_bill_real_goods")
public class ReceiveBillRealGoods { @ApiModelProperty(notes = "编码")
    private Long id;
    private Long billId;
    private Long priceTypeId;
    private Long goodsId;
    private BigDecimal discount;
    private BigDecimal tagPrice;
    private BigDecimal balancePrice;
    private BigDecimal currencyPrice;
    private BigDecimal exchangeRate;
    private BigDecimal quantity;
    private String remark;
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

    public static ReceiveBillRealGoods build() {
        ReceiveBillRealGoods item = new ReceiveBillRealGoods();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        return item;
    }

}
