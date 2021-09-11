package com.regent.rbp.task.inno.model.req;

import com.regent.rbp.task.inno.model.dto.ChannelDto;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class ChannelReqDto extends BaseRequestDto {
    private List<ChannelDto> data;
}
