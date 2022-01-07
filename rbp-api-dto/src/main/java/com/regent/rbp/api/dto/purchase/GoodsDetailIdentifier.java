package com.regent.rbp.api.dto.purchase;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * @author huangjie
 * @date : 2022/01/07
 * @description
 */
public interface GoodsDetailIdentifier {
    /**
     * 同款多价
     * @return
     */
    default String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s", this.getGoodsId(), balancePriceStr);
    }

    Long getGoodsId();

    BigDecimal getBalancePrice();
}
