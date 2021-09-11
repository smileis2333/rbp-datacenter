package com.regent.rbp.task.inno.model.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xuxing
 */
@Getter
@Setter
public class StatusResponseDto{
    private String code;
    private String msg;
    private String RowUniqueKey;
}
