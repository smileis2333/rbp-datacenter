package com.regent.rbp.task.inno.model.dto;

import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-24
 */
@Data
public class OnlineSyncGoodsStockDto {

    @ApiModelProperty(notes = "上传ERP仓库库存的唯一标示")
    private String id;

    @ApiModelProperty(notes = "具体商品商品SKU")
    private String sku;

    @ApiModelProperty(notes = "商品库存")
    private Integer nums;

    @ApiModelProperty(notes = "仓库编码")
    private String s_code;

    @ApiModelProperty(notes = "库存类型（2仓库）目前默认2")
    private String s_type;

    public static OnlineSyncGoodsStockDto build(String sku, Integer nums, String s_code) {
        OnlineSyncGoodsStockDto stockDto = new OnlineSyncGoodsStockDto();
        stockDto.setId(SnowFlakeUtil.getDefaultSnowFlakeId().toString());
        stockDto.setSku(sku);
        stockDto.setNums(nums);
        stockDto.setS_code(s_code);
        stockDto.setS_type("2");

        return stockDto;
    }

}
