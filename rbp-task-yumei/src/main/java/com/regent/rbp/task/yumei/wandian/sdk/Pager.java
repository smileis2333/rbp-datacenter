package com.regent.rbp.task.yumei.wandian.sdk;

public class Pager
{
	// 分页大小
	private int pageSize = 0;
	// 页码
	private int pageNo = 0;
	// 是否返回总页数
	private boolean calcTotal = false;

	public Pager(int pageSize, int pageNo, boolean calcTotal)
	{
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.calcTotal = calcTotal;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}

	public boolean isCalcTotal()
	{
		return calcTotal;
	}

	public void setCalcTotal(boolean calcTotal)
	{
		this.calcTotal = calcTotal;
	}
}
