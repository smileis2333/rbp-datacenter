package com.regent.rbp.task.inno.model.req;

import lombok.Data;

/**
 * @author czd
 * @date 2021/10/23
 */
@Data
public class InnoBaseReq<T> {

    private String App_key;
    private String App_secrept;

    private T Data;
}
