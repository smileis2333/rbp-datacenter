package com.regent.rbp.api.core.label;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
@TableName(value = "rbp_label")
public class Label {

    private Long id;

    private String code;

    private Long batchManagementId;

    private Long labelRuleId;

    private Long channelId;

    private Integer status;

    private String notes;

    private Long goodsId;

    private Long colorId;

    private Long longId;

    private Long sizeId;

    private BigDecimal quantity;

    private Integer exportStatus;
}