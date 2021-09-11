package com.regent.rbp.task.inno.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class ChannelRespDto extends BaseResponseDto {
    private List<StatusResponseDto> data;
}
