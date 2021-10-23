package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author czd
 * @date 2021/10/23
 */
@ToString
@Data
public class InnoBaseResp<T> {
	@ApiModelProperty(notes = "响应编码")
	private String code;
	@ApiModelProperty(notes = "响应消息")
	private String msg;
	@ApiModelProperty(notes = "响应实体类")
	private T data;

	public InnoBaseResp() {
	}

	public InnoBaseResp(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}
