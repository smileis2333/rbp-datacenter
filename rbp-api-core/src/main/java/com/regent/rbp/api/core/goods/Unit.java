package com.regent.rbp.api.core.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @Description 单位档案
 * @Author shaoqidong
 * @Date 2021/9/9
 **/
@Data
@ApiModel(description = "单位档案")
@TableName(value = "rbp_unit")
public class Unit {
    /**
     * 编码
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 创建人
     */
    private Long createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;

    public static Unit build(String name) {
        Long userId = ThreadLocalGroup.getUserId();
        Unit unit = new Unit();
        unit.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        unit.setName(name);
        unit.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        unit.setCreatedBy(userId);
        unit.setUpdatedBy(userId);
        return unit;
    }
}
