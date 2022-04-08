package com.regent.rbp.task.yumei.wandian.sdk;

import java.io.IOException;
import java.lang.reflect.Type;

public interface Client
{
	/**
	 * 设置超时时间, 毫秒
	 * 
	 * @return
	 */
	void setTimeout(int ms);

	Object excecute(String method, Object[] args, Pager pager, Type returnType) throws WdtErpException, IOException;
}
