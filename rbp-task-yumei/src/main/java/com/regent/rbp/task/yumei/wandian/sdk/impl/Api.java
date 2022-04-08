package com.regent.rbp.task.yumei.wandian.sdk.impl;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api
{
	// 方法名
	public String value();

	// 是否分页
	public boolean paged() default false;
}
