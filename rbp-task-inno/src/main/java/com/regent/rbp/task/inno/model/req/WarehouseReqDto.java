package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.WarehouseDto;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-09-22 17:43
 */
@Data
public class WarehouseReqDto extends BaseRequestDto{

    private List<WarehouseDto> data;
}
