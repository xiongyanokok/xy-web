package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class AfterHandlerImpl extends AfterHandler {

	public void afterInvoke(Object proxy, Method method, Object[] args) {
		System.out.println("JDK after");
	}
	
	public void afterInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
		System.out.println("CGLIB after");
	}
}
