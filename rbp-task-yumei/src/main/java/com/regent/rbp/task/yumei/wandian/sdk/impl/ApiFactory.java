package com.regent.rbp.task.yumei.wandian.sdk.impl;

import com.regent.rbp.task.yumei.wandian.sdk.Client;
import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.WdtErpException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class ApiFactory
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

			Pager pager = null;
			// 如果分页查询, 最后一个参数是pager
			if (api.paged())
			{
				int len = args.length;
				pager = (Pager) args[len - 1];

				Object[] newParams = new Object[len - 1];
				System.arraycopy(args, 0, newParams, 0, len - 1);
				args = newParams;
			}

			Type returnType = method.getGenericReturnType();

			return this.client.excecute(api.value(), args, pager, returnType);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Client client, Class<T> iface)
	{
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface }, new DynamicProxy(client));
	}
}
