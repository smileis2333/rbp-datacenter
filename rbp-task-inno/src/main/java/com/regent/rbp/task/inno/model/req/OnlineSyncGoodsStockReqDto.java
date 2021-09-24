package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.OnlineSyncGoodsStockDto;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-24
 */
@Data
public class OnlineSyncGoodsStockReqDto extends BaseRequestDto {

    private List<OnlineSyncGoodsStockDto> data;

}
