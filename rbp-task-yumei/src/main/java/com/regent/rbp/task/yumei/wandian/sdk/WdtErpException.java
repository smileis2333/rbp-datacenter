package com.regent.rbp.task.yumei.wandian.sdk;

@SuppressWarnings("serial")
public class WdtErpException extends Exception
{
	private int code;

	public WdtErpException(int code, String message)
	{
		super(message);
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
}
