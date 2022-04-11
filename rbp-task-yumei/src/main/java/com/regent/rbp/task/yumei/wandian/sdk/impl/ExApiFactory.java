package com.regent.rbp.task.yumei.wandian.sdk.impl;


import com.regent.rbp.task.yumei.wandian.sdk.Client;
import com.regent.rbp.task.yumei.wandian.sdk.WdtErpException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ExApiFactory
{
	private static class DynamicProxy implements InvocationHandler
	{
		private Client client;

		public DynamicProxy(Client client)
		{
			this.client = client;
		}

		@Override
		public Object invoke(Object inst, Method method, Object[] args) throws Throwable
		{
			Api api = method.getAnnotation(Api.class);
			if (api == null)
				throw new WdtErpException(-1, "Api Name not indicated");

			// 如果分页查询, 最后一个参数是pager
			if (api.paged())
				throw new IllegalArgumentException("Pagination is not supported for Script Api");

			int len = args.length;

			Object[] newParams = new Object[len + 1];
			newParams[0] = api.value();
			System.arraycopy(args, 0, newParams, 1, len);

			return this.client.excecute("system.ScriptExtension.call", newParams, null, method.getReturnType());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Client client, Class<T> iface)
	{
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface }, new DynamicProxy(client));
	}

}
