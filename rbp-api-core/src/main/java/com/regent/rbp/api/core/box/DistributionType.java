package com.regent.rbp.api.core.box;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 配码类型 对象 rbp_distribution_type
 * @author czw
 * @date 2020-07-07
 */
@Data
@TableName(value = "rbp_distribution_type")
public class DistributionType {
    private Long id;

    private String code;

    private String name;

    private Integer status;

    private Long sizeClassId;

    private BigDecimal quantity;

}
