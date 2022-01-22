package com.regent.rbp.api.core.box;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
@TableName(value = "rbp_box_detail")
public class BoxDetail {
    private Long id;

    private Long boxId;

    private Long goodsId;

    private Long colorId;

    private Long longId;

    private Long sizeId;

    private BigDecimal quantity;

}
