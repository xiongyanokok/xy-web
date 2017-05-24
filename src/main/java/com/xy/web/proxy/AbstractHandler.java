package com.xy.web.proxy;

import java.lang.reflect.InvocationHandler;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 抽象类
 * 
 * @author xiongyan
 * @date 2017年2月13日 下午2:57:54
 */
public abstract class AbstractHandler implements InvocationHandler, MethodInterceptor {
	
	private Object targetObject;
	
	private Boolean cglib;

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
	
	public Boolean getCglib() {
		return cglib;
	}

	public void setCglib(Boolean cglib) {
		this.cglib = cglib;
	}

}
