package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

import com.google.gson.annotations.SerializedName;

public class TransferOrderCreateResponse
{
	/*
	 * { "status":0 "data": { "message":"CK2020072018" "status":20 } }
	 */
	private String message;
	private Integer status;

	@SerializedName("data")
	private DataDto data;

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public static class DataDto
	{
		private String message;
		private Integer status;

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public Integer getStatus()
		{
			return status;
		}

		public void setStatus(Integer status)
		{
			this.status = status;
		}

	}

	@Override
	public String toString()
	{
		return "CreateTransferStockinResponse [message=" + message + ", status=" + status + ", data=" + data + "]";
	}

}
